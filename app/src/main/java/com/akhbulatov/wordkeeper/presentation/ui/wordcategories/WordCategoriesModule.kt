package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WordCategoriesModule {
    @Binds
    @IntoMap
    @ViewModelKey(WordCategoriesViewModel::class)
    abstract fun bindWordCategoriesViewModel(viewModel: WordCategoriesViewModel): ViewModel
}