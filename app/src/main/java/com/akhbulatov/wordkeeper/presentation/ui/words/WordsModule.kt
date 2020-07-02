package com.akhbulatov.wordkeeper.presentation.ui.words

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class WordsModule {
    @Binds
    abstract fun bindWordsViewModel(viewModel: WordsViewModel): ViewModel
}
