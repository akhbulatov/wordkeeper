package com.akhbulatov.wordkeeper.features.word.addeditword

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class AddEditWordModule {
    @Binds
    abstract fun bindAddEditWordViewModel(viewModel: AddEditWordViewModel): ViewModel
}
