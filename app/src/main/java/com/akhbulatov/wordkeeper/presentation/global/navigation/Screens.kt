package com.akhbulatov.wordkeeper.presentation.global.navigation

import androidx.fragment.app.Fragment
import com.akhbulatov.wordkeeper.presentation.ui.about.AboutFragment
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object Words : SupportAppScreen() {
        override fun getFragment(): Fragment = WordsFragment()
    }

    object About : SupportAppScreen() {
        override fun getFragment(): Fragment = AboutFragment()
    }
}