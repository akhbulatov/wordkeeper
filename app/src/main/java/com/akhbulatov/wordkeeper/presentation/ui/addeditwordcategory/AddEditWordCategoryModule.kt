package com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditWordCategoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddEditWordCategoryViewModel::class)
    abstract fun bindAddEditWordCategoryViewModel(viewModel: AddEditWordCategoryViewModel): ViewModel
}