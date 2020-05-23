package com.akhbulatov.wordkeeper.presentation.ui.deletewordcategory

import androidx.lifecycle.ViewModel
import com.akhbulatov.wordkeeper.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DeleteWordCategoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(DeleteWordCategoryViewModel::class)
    abstract fun bindDeleteWordCategoryViewModel(viewModel: DeleteWordCategoryViewModel): ViewModel
}