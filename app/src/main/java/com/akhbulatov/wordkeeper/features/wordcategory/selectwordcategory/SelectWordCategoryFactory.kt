package com.akhbulatov.wordkeeper.features.wordcategory.selectwordcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.WordKeeperApp

class SelectWordCategoryFactory {

    fun createViewModelFactory() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val appFactory = WordKeeperApp.appFactory
            @Suppress("UNCHECKED_CAST")
            return SelectWordCategoryViewModel(
                getWordCategoriesUseCase = appFactory.wordCategoryDomainFactory.getWordCategoriesUseCase
            ) as T
        }
    }
}
