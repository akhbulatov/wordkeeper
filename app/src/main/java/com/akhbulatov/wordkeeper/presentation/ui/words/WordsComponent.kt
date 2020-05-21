package com.akhbulatov.wordkeeper.presentation.ui.words

import com.akhbulatov.wordkeeper.di.FragmentScope
import dagger.Subcomponent

@Subcomponent(modules = [WordsModule::class])
@FragmentScope
interface WordsComponent {
    fun inject(fragment: WordsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): WordsComponent
    }
}