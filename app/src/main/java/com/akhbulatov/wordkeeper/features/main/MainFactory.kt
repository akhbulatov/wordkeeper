package com.akhbulatov.wordkeeper.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.WordKeeperApp

class MainFactory {

    fun createViewModelFactory() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val appFactory = WordKeeperApp.appFactory
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                router = appFactory.navigationFactory.router
            ) as T
        }
    }
}
