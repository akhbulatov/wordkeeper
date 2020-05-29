package com.akhbulatov.wordkeeper.di.modules

import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.presentation.global.mvvm.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
