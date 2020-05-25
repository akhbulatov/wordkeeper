package com.akhbulatov.wordkeeper.presentation.ui.main

import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.presentation.global.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel() {

    fun onStart() {
        router.newRootScreen(Screens.Words)
    }

    fun onWordsClicked() {
        TODO("Not yet implemented")
    }

    fun onWordCategoriesClicked() {
        TODO("Not yet implemented")
    }

    fun onRateAppClicked() {
        TODO("Not yet implemented")
    }

    fun onAboutClicked() {
        router.navigateTo(Screens.About)
    }

    fun backToFirstScreen() {
        router.backTo(Screens.Words)
    }
}