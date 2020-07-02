package com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class SelectWordCategoryModule {
    @Binds
    abstract fun bindSelectWordCategoryViewModel(viewModel: SelectWordCategoryViewModel): ViewModel
}
