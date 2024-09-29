package com.akhbulatov.wordkeeper.features.word.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.WordKeeperApp

class WordsFactory {

    fun createViewModelFactory() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val appFactory = WordKeeperApp.appFactory
            @Suppress("UNCHECKED_CAST")
            return WordsViewModel(
                getWordsUseCase = appFactory.wordDomainFactory.getWordsUseCase,
                editWordUseCase = appFactory.wordDomainFactory.editWordUseCase,
                deleteWordsUseCase = appFactory.wordDomainFactory.deleteWordsUseCase,
                getWordSortModeUseCase = appFactory.wordDomainFactory.getWordSortModeUseCase,
                setWordSortModeUseCase = appFactory.wordDomainFactory.setWordSortModeUseCase
            ) as T
        }
    }
}
