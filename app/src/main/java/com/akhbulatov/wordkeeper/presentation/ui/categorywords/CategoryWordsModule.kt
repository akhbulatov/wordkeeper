package com.akhbulatov.wordkeeper.presentation.ui.categorywords

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module()
abstract class CategoryWordsModule {
    @Binds
    abstract fun bindCategoryWordsViewModel(viewModel: CategoryWordsViewModel): ViewModel
}
