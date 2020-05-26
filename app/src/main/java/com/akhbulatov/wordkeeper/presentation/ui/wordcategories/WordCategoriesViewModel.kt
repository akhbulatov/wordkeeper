package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.presentation.global.navigation.Screens
import com.akhbulatov.wordkeeper.presentation.ui.global.models.toUiModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class WordCategoriesViewModel @Inject constructor(
    private val router: Router,
    private val wordCategoryInteractor: WordCategoryInteractor
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val currentViewState: ViewState get() = _viewState.value!!

    init {
        _viewState.value = ViewState()
    }

    fun loadWordCategories() {
        viewModelScope.launch {
            wordCategoryInteractor.getWordCategories()
                .onStart { _viewState.value = currentViewState.copy(emptyProgress = true) }
                .onEach { _viewState.value = currentViewState.copy(emptyProgress = false) }
                .catch { _viewState.value = currentViewState.copy(emptyError = Pair(true, it.message)) }
                .collect {
                    if (it.isNotEmpty()) {
                        _viewState.value = currentViewState.copy(
                            emptyData = false,
                            emptyError = Pair(false, null),
                            wordCategories = Pair(true, it)
                        )
                    } else {
                        _viewState.value = currentViewState.copy(
                            emptyData = true,
                            emptyError = Pair(false, null),
                            wordCategories = Pair(false, it)
                        )
                    }
                }
        }
    }

    fun onWordCategoryClicked(wordCategory: WordCategory) {
        router.navigateTo(Screens.WordsOfCategory(wordCategory.toUiModel()))
    }

    fun onAddWordCategoryClicked(name: String) {
        val wordCategory = WordCategory(name = name)
        viewModelScope.launch {
            wordCategoryInteractor.addWordCategory(wordCategory)
        }
    }

    fun onEditWordCategoryClicked(id: Long, name: String) {
        val wordCategory = WordCategory(
            id = id,
            name = name
        )
        viewModelScope.launch {
            wordCategoryInteractor.editWordCategory(wordCategory)
        }
    }

    fun onDeleteWordCategoryWithWordsClicked(wordCategory: WordCategory) {
        viewModelScope.launch {
            wordCategoryInteractor.deleteWordCategoryWithWords(wordCategory)
        }
    }

    data class ViewState(
        val emptyProgress: Boolean = false,
        val emptyData: Boolean = false,
        val emptyError: Pair<Boolean, String?> = Pair(false, null),
        val wordCategories: Pair<Boolean, List<WordCategory>> = Pair(false, emptyList())
    )
}