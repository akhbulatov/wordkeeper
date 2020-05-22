package com.akhbulatov.wordkeeper.presentation.ui.sortword

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SortWordModule {
    @Binds
    @IntoMap
    @ViewModelKey(SortWordViewModel::class)
    abstract fun bindSortWordViewModel(viewModel: SortWordViewModel): ViewModel
}