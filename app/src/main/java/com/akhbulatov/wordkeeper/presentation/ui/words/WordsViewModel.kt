package com.akhbulatov.wordkeeper.presentation.ui.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.core.ui.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.domain.word.models.Word
import com.akhbulatov.wordkeeper.domain.word.usecases.DeleteWordsUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.EditWordUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.GetWordSortModeUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.GetWordsUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.SetWordSortModeUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordsViewModel @Inject constructor(
    private val getWordsUseCase: GetWordsUseCase,
    private val editWordUseCase: EditWordUseCase,
    private val deleteWordsUseCase: DeleteWordsUseCase,
    private val getWordSortModeUseCase: GetWordSortModeUseCase,
    private val setWordSortModeUseCase: SetWordSortModeUseCase
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
            getWordsUseCase.invoke()
                .onStart { _viewState.value = currentViewState.copy(emptyProgress = true) }
                .onEach { _viewState.value = currentViewState.copy(emptyProgress = false) }
                .catch { ex ->
                    _viewState.value = currentViewState.copy(
                        emptyProgress = false,
                        emptyError = Pair(true, ex.message)
                    )
                }
                .collect { words ->
                    loadedWords = words

                    if (words.isNotEmpty()) {
                        _viewState.value = currentViewState.copy(
                            emptyData = false,
                            emptyError = Pair(false, null),
                            words = Pair(true, words)
                        )
                    } else {
                        _viewState.value = currentViewState.copy(
                            emptyData = true,
                            emptyError = Pair(false, null),
                            words = Pair(false, words)
                        )
                    }
                }
        }
    }

    fun onSelectWordCatalogClicked(words: MutableList<Word>, category: String) {
        val newWords = words.map { it.copy(category = category) }
        viewModelScope.launch {
            newWords.forEach { editWordUseCase.invoke(it) }
        }
    }

    fun onDeleteWordsClicked(words: List<Word>) {
        viewModelScope.launch {
            deleteWordsUseCase.invoke(words)
        }
    }

    fun onSearchWordChanged(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                val foundWords = loadedWords.filter { word ->
                    word.name.startsWith(query, ignoreCase = true)
                }
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

    fun getWordSortMode(): Word.SortMode = getWordSortModeUseCase.invoke()

    fun onSortWordSelected(sortMode: Word.SortMode) {
        setWordSortModeUseCase.invoke(sortMode)
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
