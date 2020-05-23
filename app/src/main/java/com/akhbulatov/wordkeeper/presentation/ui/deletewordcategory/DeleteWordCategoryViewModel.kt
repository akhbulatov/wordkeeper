package com.akhbulatov.wordkeeper.presentation.ui.deletewordcategory

import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel
import com.akhbulatov.wordkeeper.presentation.ui.global.models.toDomainModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteWordCategoryViewModel @Inject constructor(
    private val wordCategoryInteractor: WordCategoryInteractor
) : BaseViewModel() {

    fun onDeleteWordCategoryWithWordsClicked(wordCategory: WordCategoryUiModel) {
        viewModelScope.launch {
            wordCategoryInteractor.deleteWordCategoryWithWords(wordCategory.toDomainModel())
        }
    }

    override fun onBackPressed() {}
}