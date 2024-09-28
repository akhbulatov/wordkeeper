package com.akhbulatov.wordkeeper

import com.akhbulatov.wordkeeper.features.about.AboutFragment
import com.akhbulatov.wordkeeper.features.word.words.WordsFragment
import com.akhbulatov.wordkeeper.features.wordcategory.categorywords.CategoryWordsFragment
import com.akhbulatov.wordkeeper.features.wordcategory.wordcategories.WordCategoriesFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun words() = FragmentScreen("Words") { WordsFragment() }

    fun wordCategories() = FragmentScreen("WordCategories") { WordCategoriesFragment() }

    fun categoryWords(category: String) = FragmentScreen("CategoryWords") {
        CategoryWordsFragment.newInstance(category)
    }

    fun about() = FragmentScreen("About") { AboutFragment() }
}
