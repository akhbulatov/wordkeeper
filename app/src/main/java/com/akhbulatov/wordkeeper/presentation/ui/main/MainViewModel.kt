package com.akhbulatov.wordkeeper.presentation.ui.main

import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.presentation.global.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel() {

    fun onAboutClicked() = router.navigateTo(Screens.About)

    override fun onBackPressed() = router.exit()
}