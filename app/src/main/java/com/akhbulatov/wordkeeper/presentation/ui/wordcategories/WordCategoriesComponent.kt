package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import com.akhbulatov.wordkeeper.App
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
        fun create(): WordCategoriesComponent = App.appComponent
            .wordCategoriesComponentFactory()
            .create()
    }
}
