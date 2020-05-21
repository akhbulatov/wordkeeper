package com.akhbulatov.wordkeeper.presentation.ui.addeditword

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditWordModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddEditWordViewModel::class)
    abstract fun bindAddEditWordViewModel(viewModel: AddEditWordViewModel): ViewModel
}