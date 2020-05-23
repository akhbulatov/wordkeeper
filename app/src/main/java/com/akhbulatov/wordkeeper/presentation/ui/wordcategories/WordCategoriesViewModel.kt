package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordCategoriesViewModel @Inject constructor(
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

    override fun onBackPressed() {}

    data class ViewState(
        val emptyProgress: Boolean = false,
        val emptyData: Boolean = false,
        val emptyError: Pair<Boolean, String?> = Pair(false, null),
        val wordCategories: Pair<Boolean, List<WordCategory>> = Pair(false, emptyList())
    )
}