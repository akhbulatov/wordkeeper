package com.akhbulatov.wordkeeper.features.main

import com.akhbulatov.wordkeeper.ExternalScreens
import com.akhbulatov.wordkeeper.Screens
import com.akhbulatov.wordkeeper.core.ui.mvvm.BaseViewModel
import com.github.terrakok.cicerone.Router

class MainViewModel(
    private val router: Router
) : BaseViewModel() {

    fun onStart() {
        onWordsClicked()
    }

    fun onWordsClicked() {
        router.newRootScreen(Screens.words())
    }

    fun onWordCategoriesClicked() {
        router.replaceScreen(Screens.wordCategories())
    }

    fun onRateAppClicked(appUrl: String) {
        router.navigateTo(ExternalScreens.externalBrowser(appUrl))
    }

    fun onAboutClicked() {
        router.replaceScreen(Screens.about())
    }
}
