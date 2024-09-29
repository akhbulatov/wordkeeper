package com.akhbulatov.wordkeeper.features.wordcategory.wordcategories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.WordKeeperApp

class WordCategoriesFactory {

    fun createViewModelFactory() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val appFactory = WordKeeperApp.appFactory
            @Suppress("UNCHECKED_CAST")
            return WordCategoriesViewModel(
                router = appFactory.navigationFactory.router,
                getWordCategoriesUseCase = appFactory.wordCategoryDomainFactory.getWordCategoriesUseCase,
                addWordCategoryUseCase = appFactory.wordCategoryDomainFactory.addWordCategoryUseCase,
                editWordCategoryUseCase = appFactory.wordCategoryDomainFactory.editWordCategoryUseCase,
                deleteWordCategoryWithWordsUseCase = appFactory.wordCategoryDomainFactory.deleteWordCategoryWithWordsUseCase
            ) as T
        }
    }
}
