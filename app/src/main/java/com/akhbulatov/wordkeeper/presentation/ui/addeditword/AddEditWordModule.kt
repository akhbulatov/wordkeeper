package com.akhbulatov.wordkeeper.presentation.ui.addeditword

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class AddEditWordModule {
    @Binds
    abstract fun bindAddEditWordViewModel(viewModel: AddEditWordViewModel): ViewModel
}
