package com.akhbulatov.wordkeeper.core.ui.navigation

import com.akhbulatov.wordkeeper.presentation.ui.about.AboutFragment
import com.akhbulatov.wordkeeper.presentation.ui.categorywords.CategoryWordsFragment
import com.akhbulatov.wordkeeper.presentation.ui.wordcategories.WordCategoriesFragment
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun words() = FragmentScreen("Words") { WordsFragment() }

    fun wordCategories() = FragmentScreen("WordCategories") { WordCategoriesFragment() }

    fun categoryWords(category: String) = FragmentScreen("CategoryWords") {
        CategoryWordsFragment.newInstance(category)
    }

    fun about() = FragmentScreen("About") { AboutFragment() }
}
