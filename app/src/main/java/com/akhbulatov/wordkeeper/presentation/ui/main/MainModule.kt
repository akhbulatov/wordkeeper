package com.akhbulatov.wordkeeper.presentation.ui.main

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class MainModule {
    @Binds
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}
