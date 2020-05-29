package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import com.akhbulatov.wordkeeper.di.FragmentScope
import dagger.Subcomponent

@Subcomponent(modules = [WordCategoriesModule::class])
@FragmentScope
interface WordCategoriesComponent {
    fun inject(fragment: WordCategoriesFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): WordCategoriesComponent
    }
}
