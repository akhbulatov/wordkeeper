package com.akhbulatov.wordkeeper.features.wordcategory.categorywords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.WordKeeperApp

class CategoryWordsFactory {

    fun createViewModelFactory() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val appFactory = WordKeeperApp.appFactory
            @Suppress("UNCHECKED_CAST")
            return CategoryWordsViewModel(
                router = appFactory.navigationFactory.router,
                getWordsByCategoryUseCase = appFactory.wordDomainFactory.getWordsByCategoryUseCase,
            ) as T
        }
    }
}
