package com.akhbulatov.wordkeeper.presentation.ui.words

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WordsModule {
    @Binds
    @IntoMap
    @ViewModelKey(WordsViewModel::class)
    abstract fun bindWordsViewModel(viewModel: WordsViewModel): ViewModel
}
