package com.akhbulatov.wordkeeper.presentation.ui.words

import com.akhbulatov.wordkeeper.di.FragmentScope
import com.akhbulatov.wordkeeper.presentation.global.mvvm.ViewModelModule
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WordsModule::class, ViewModelModule::class])
interface WordsComponent {
    fun inject(fragment: WordsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): WordsComponent
    }
}
