package com.akhbulatov.wordkeeper.features.wordcategory.wordcategories

import com.akhbulatov.wordkeeper.WordKeeperApp
import com.akhbulatov.wordkeeper.core.ui.mvvm.ViewModelModule
import com.akhbulatov.wordkeeper.di.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WordCategoriesModule::class, ViewModelModule::class])
interface WordCategoriesComponent {
    fun inject(fragment: WordCategoriesFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): WordCategoriesComponent
    }

    companion object {
        fun create(): WordCategoriesComponent = WordKeeperApp.appComponent
            .wordCategoriesComponentFactory()
            .create()
    }
}
