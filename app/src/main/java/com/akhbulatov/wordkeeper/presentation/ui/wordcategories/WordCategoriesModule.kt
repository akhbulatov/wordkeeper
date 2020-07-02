package com.akhbulatov.wordkeeper.presentation.ui.wordcategories

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class WordCategoriesModule {
    @Binds
    abstract fun bindWordCategoriesViewModel(viewModel: WordCategoriesViewModel): ViewModel
}
