package com.akhbulatov.wordkeeper.presentation.ui.categorywords

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CategoryWordsModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoryWordsViewModel::class)
    abstract fun bindCategoryWordsViewModel(viewModel: CategoryWordsViewModel): ViewModel
}