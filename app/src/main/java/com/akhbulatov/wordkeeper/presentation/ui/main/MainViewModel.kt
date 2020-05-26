package com.akhbulatov.wordkeeper.presentation.ui.main

import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.presentation.global.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel() {

    fun onStart() {
        onWordsClicked()
    }

    fun onWordsClicked() {
        router.newRootScreen(Screens.Words)
    }

    fun onWordCategoriesClicked() {
        router.replaceScreen(Screens.WordCategories)
    }

    fun onRateAppClicked(appUrl: String) {
        router.navigateTo(Screens.ExternalBrowser(appUrl))
    }

    fun onAboutClicked() {
        router.replaceScreen(Screens.About)
    }
}