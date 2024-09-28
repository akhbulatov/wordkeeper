package com.akhbulatov.wordkeeper.features.wordcategory.selectwordcategory

import com.akhbulatov.wordkeeper.core.ui.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.GetWordCategoriesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SelectWordCategoryViewModel @Inject constructor(
    private val getWordCategoriesUseCase: GetWordCategoriesUseCase
) : BaseViewModel() {

    fun getWordCategories(): Array<String> =
        runBlocking {
            getWordCategoriesUseCase.invoke().first()
                .map { it.name }
                .toTypedArray()
        }
}
