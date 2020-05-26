package com.akhbulatov.wordkeeper.presentation.global.navigation

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.akhbulatov.wordkeeper.presentation.ui.about.AboutFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel
import com.akhbulatov.wordkeeper.presentation.ui.wordcategories.WordCategoriesFragment
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsFragment
import com.akhbulatov.wordkeeper.presentation.ui.wordsofcategory.WordsOfCategoryFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object Words : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return WordsFragment()
        }
    }

    object WordCategories : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return WordCategoriesFragment()
        }
    }

    data class WordsOfCategory(
        val wordCategory: WordCategoryUiModel
    ) : SupportAppScreen() {
        override fun getFragment(): Fragment = WordsOfCategoryFragment.newInstance(wordCategory)
    }

    object About : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return AboutFragment()
        }
    }

    // --- External --- //
    data class ExternalBrowser(
        val url: String
    ) : SupportAppScreen() {

        override fun getActivityIntent(context: Context): Intent? {
            return Intent(Intent.ACTION_VIEW, url.toUri())
        }
    }
    // --- External --- //
}