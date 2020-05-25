package com.akhbulatov.wordkeeper.presentation.global.navigation

import androidx.fragment.app.Fragment
import com.akhbulatov.wordkeeper.presentation.ui.about.AboutFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsFragment
import com.akhbulatov.wordkeeper.presentation.ui.wordsofcategory.WordsOfCategoryFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object Words : SupportAppScreen() {
        override fun getFragment(): Fragment = WordsFragment()
    }


    data class WordsOfCategory(
        val wordCategory: WordCategoryUiModel
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment = WordsOfCategoryFragment.newInstance(wordCategory)
    }

    object About : SupportAppScreen() {
        override fun getFragment(): Fragment = AboutFragment()
    }
}