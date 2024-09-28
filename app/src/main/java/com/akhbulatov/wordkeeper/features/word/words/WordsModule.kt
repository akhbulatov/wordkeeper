package com.akhbulatov.wordkeeper.features.word.words

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class WordsModule {
    @Binds
    abstract fun bindWordsViewModel(viewModel: WordsViewModel): ViewModel
}
