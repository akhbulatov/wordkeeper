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
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class WordsViewModel @Inject constructor(
    private val router: Router,
    private val wordInteractor: WordInteractor
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val currentViewState: ViewState get() = _viewState.value!!

    init {
        _viewState.value = ViewState()
    }

    fun loadWords() {
        viewModelScope.launch {
            wordInteractor.getWords(Word.SortMode.NAME) // todo
                .onStart { _viewState.value = currentViewState.copy(emptyProgress = true) }
                .onEach { _viewState.value = currentViewState.copy(emptyProgress = false) }
                .catch { _viewState.value = currentViewState.copy(emptyError = Pair(true, it.message)) }
                .collect {
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

    fun onDeleteWordsClicked(words: List<Word>) {
        viewModelScope.launch {
            wordInteractor.deleteWords(words)
        }
    }

    override fun onBackPressed() = router.exit()

    data class ViewState(
        val emptyProgress: Boolean = false,
        val emptyData: Boolean = false,
        val emptyError: Pair<Boolean, String?> = Pair(false, null),
        val words: Pair<Boolean, List<Word>> = Pair(false, emptyList())
    )
}