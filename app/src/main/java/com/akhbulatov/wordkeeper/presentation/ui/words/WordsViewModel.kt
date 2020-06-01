package com.akhbulatov.wordkeeper.presentation.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.domain.word.WordInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordsViewModel @Inject constructor(
    private val wordInteractor: WordInteractor
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val currentViewState: ViewState
        get() = _viewState.value!!

    private var loadedWords = listOf<Word>()

    init {
        _viewState.value = ViewState()
    }

    fun loadWords() {
        viewModelScope.launch {
            wordInteractor.getWords()
                .onStart { _viewState.value = currentViewState.copy(emptyProgress = true) }
                .onEach { _viewState.value = currentViewState.copy(emptyProgress = false) }
                .catch {
                    _viewState.value = currentViewState.copy(
                        emptyProgress = false,
                        emptyError = Pair(true, it.message)
                    )
                }
                .collect {
                    loadedWords = it

                    if (it.isNotEmpty()) {
                        _viewState.value = currentViewState.copy(
                            emptyData = false,
                            emptyError = Pair(false, null),
                            words = Pair(true, it)
                        )
                    } else {
                        _viewState.value = currentViewState.copy(
                            emptyData = true,
                            emptyError = Pair(false, null),
                            words = Pair(false, it)
                        )
                    }
                }
        }
    }

    fun onSelectWordCatalogClicked(words: MutableList<Word>, category: String) {
        val newWords = words.map { it.copy(category = category) }
        viewModelScope.launch {
            newWords.forEach { wordInteractor.editWord(it) }
        }
    }

    fun onDeleteWordsClicked(words: List<Word>) {
        viewModelScope.launch {
            wordInteractor.deleteWords(words)
        }
    }

    fun onSearchWordChanged(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                val foundWords = wordInteractor.searchWords(query, loadedWords)
                _viewState.value = currentViewState.copy(
                    emptySearchResult = Pair(foundWords.isEmpty(), query),
                    words = Pair(true, foundWords)
                )
            } else {
                _viewState.value = currentViewState.copy(
                    emptySearchResult = Pair(false, null),
                    words = Pair(true, loadedWords)
                )
            }
        }
    }

    fun onCloseSearchWordClicked() {
        _viewState.value = currentViewState.copy(
            emptySearchResult = Pair(false, null),
            words = Pair(true, loadedWords)
        )
    }

    fun getWordSortMode(): Word.SortMode = wordInteractor.wordSortMode

    fun onSortWordSelected(sortMode: Word.SortMode) {
        wordInteractor.wordSortMode = sortMode
        loadWords()
    }

    data class ViewState(
        val emptyProgress: Boolean = false,
        val emptyData: Boolean = false,
        val emptyError: Pair<Boolean, String?> = Pair(false, null),
        val emptySearchResult: Pair<Boolean, String?> = Pair(false, null),
        val words: Pair<Boolean, List<Word>> = Pair(false, emptyList())
    )
}
