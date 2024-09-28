package com.akhbulatov.wordkeeper.features.wordcategory.wordcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.core.ui.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.core.ui.navigation.Screens
import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.AddWordCategoryUseCase
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.DeleteWordCategoryWithWordsUseCase
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.EditWordCategoryUseCase
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.GetWordCategoriesUseCase
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordCategoriesViewModel @Inject constructor(
    private val router: Router,
    private val getWordCategoriesUseCase: GetWordCategoriesUseCase,
    private val addWordCategoryUseCase: AddWordCategoryUseCase,
    private val editWordCategoryUseCase: EditWordCategoryUseCase,
    private val deleteWordCategoryWithWordsUseCase: DeleteWordCategoryWithWordsUseCase
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
            getWordCategoriesUseCase.invoke()
                .onStart { _viewState.value = currentViewState.copy(emptyProgress = true) }
                .onEach { _viewState.value = currentViewState.copy(emptyProgress = false) }
                .catch { ex ->
                    _viewState.value = currentViewState.copy(
                        emptyProgress = false,
                        emptyError = Pair(true, ex.message)
                    )
                }
                .collect { wordCategories ->
                    loadedWordCategories = wordCategories

                    if (wordCategories.isNotEmpty()) {
                        _viewState.value = currentViewState.copy(
                            emptyData = false,
                            emptyError = Pair(false, null),
                            wordCategories = Pair(true, wordCategories)
                        )
                    } else {
                        _viewState.value = currentViewState.copy(
                            emptyData = true,
                            emptyError = Pair(false, null),
                            wordCategories = Pair(false, wordCategories)
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
            addWordCategoryUseCase.invoke(wordCategory)
        }
    }

    fun onEditWordCategoryClicked(id: Long, name: String) {
        val wordCategory = WordCategory(
            id = id,
            name = name
        )
        viewModelScope.launch {
            editWordCategoryUseCase.invoke(wordCategory)
        }
    }

    fun onDeleteWordCategoryWithWordsClicked(wordCategory: WordCategory) {
        viewModelScope.launch {
            deleteWordCategoryWithWordsUseCase.invoke(wordCategory)
        }
    }

    fun onSearchWordCategoryChanged(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                val foundWords = loadedWordCategories.filter { wordCategory ->
                    wordCategory.name.startsWith(query, ignoreCase = true)
                }
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
