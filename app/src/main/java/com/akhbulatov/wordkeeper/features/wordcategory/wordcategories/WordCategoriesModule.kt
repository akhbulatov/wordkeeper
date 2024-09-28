package com.akhbulatov.wordkeeper.features.wordcategory.wordcategories

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class WordCategoriesModule {
    @Binds
    abstract fun bindWordCategoriesViewModel(viewModel: WordCategoriesViewModel): ViewModel
}
