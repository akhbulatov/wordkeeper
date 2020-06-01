package com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SelectWordCategoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(SelectWordCategoryViewModel::class)
    abstract fun bindSelectWordCategoryViewModel(viewModel: SelectWordCategoryViewModel): ViewModel
}
