package com.akhbulatov.wordkeeper.features.word.addeditword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.WordKeeperApp

class AddEditWordFactory {

    fun createViewModelFactory() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val appFactory = WordKeeperApp.appFactory
            @Suppress("UNCHECKED_CAST")
            return AddEditWordViewModel(
                addWordUseCase = appFactory.wordDomainFactory.addWordUseCase,
                editWordUseCase = appFactory.wordDomainFactory.editWordUseCase,
                getWordCategoriesUseCase = appFactory.wordCategoryDomainFactory.getWordCategoriesUseCase
            ) as T
        }
    }
}
