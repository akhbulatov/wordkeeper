package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.presentation.global.navigation.Screens
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordCategoriesViewModel @Inject constructor(
    private val router: Router,
    private val wordCategoryInteractor: WordCategoryInteractor
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> get() = _viewState

    private val currentViewState: ViewState
        get() = _viewState.value!!

    private var loadedWordCategories = listOf<WordCategory>()

    init {
        _viewState.value = ViewState()
    }

    fun loadWordCategories() {
        viewModelScope.launch {
            wordCategoryInteractor.getWordCategories()
                .onStart { _viewState.value = currentViewState.copy(emptyProgress = true) }
                .onEach { _viewState.value = currentViewState.copy(emptyProgress = false) }
                .catch {
                    _viewState.value = currentViewState.copy(
                        emptyProgress = false,
                        emptyError = Pair(true, it.message)
                    )
                }
                .collect {
                    loadedWordCategories = it

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
        router.navigateTo(Screens.categoryWords(wordCategory.name))
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

    fun onSearchWordCategoryChanged(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                val foundWords = wordCategoryInteractor.searchWordCategories(query, loadedWordCategories)
                _viewState.value = currentViewState.copy(
                    emptySearchResult = Pair(foundWords.isEmpty(), query),
                    wordCategories = Pair(true, foundWords)
                )
            } else {
                _viewState.value = currentViewState.copy(
                    emptySearchResult = Pair(false, null),
                    wordCategories = Pair(true, loadedWordCategories)
                )
            }
        }
    }

    fun onCloseSearchWordCategoryClicked() {
        _viewState.value = currentViewState.copy(
            emptySearchResult = Pair(false, null),
            wordCategories = Pair(true, loadedWordCategories)
        )
    }

    data class ViewState(
        val emptyProgress: Boolean = false,
        val emptyData: Boolean = false,
        val emptyError: Pair<Boolean, String?> = Pair(false, null),
        val emptySearchResult: Pair<Boolean, String?> = Pair(false, null),
        val wordCategories: Pair<Boolean, List<WordCategory>> = Pair(false, emptyList())
    )
}
