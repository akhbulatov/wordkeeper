package com.akhbulatov.wordkeeper.presentation.global.navigation

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.akhbulatov.wordkeeper.presentation.ui.about.AboutFragment
import com.akhbulatov.wordkeeper.presentation.ui.categorywords.CategoryWordsFragment
import com.akhbulatov.wordkeeper.presentation.ui.wordcategories.WordCategoriesFragment
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsFragment
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

    data class CategoryWords(
        val category: String
    ) : SupportAppScreen() {

        override fun getFragment(): Fragment? {
            return CategoryWordsFragment.newInstance(category)
        }
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