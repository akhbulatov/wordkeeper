package com.akhbulatov.wordkeeper.presentation.ui.main

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}
