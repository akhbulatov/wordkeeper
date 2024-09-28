package com.akhbulatov.wordkeeper.presentation.ui.main

import com.akhbulatov.wordkeeper.core.ui.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.core.ui.navigation.ExternalScreens
import com.akhbulatov.wordkeeper.core.ui.navigation.Screens
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
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
