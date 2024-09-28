package com.akhbulatov.wordkeeper.features.word.words

import com.akhbulatov.wordkeeper.WordKeeperApp
import com.akhbulatov.wordkeeper.core.ui.mvvm.ViewModelModule
import com.akhbulatov.wordkeeper.di.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WordsModule::class, ViewModelModule::class])
interface WordsComponent {
    fun inject(fragment: WordsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): WordsComponent
    }

    companion object {
        fun create(): WordsComponent = WordKeeperApp.appComponent
            .wordsComponentFactory()
            .create()
    }
}
