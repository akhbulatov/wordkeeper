package com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory

import com.akhbulatov.wordkeeper.core.ui.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SelectWordCategoryViewModel @Inject constructor(
    private val wordCategoryInteractor: WordCategoryInteractor
) : BaseViewModel() {

    fun getWordCategories(): Array<String> =
        runBlocking {
            wordCategoryInteractor.getWordCategories()
                .first()
                .map { it.name }
                .toTypedArray()
        }
}
