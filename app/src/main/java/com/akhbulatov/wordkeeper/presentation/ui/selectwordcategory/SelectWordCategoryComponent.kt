package com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory

import com.akhbulatov.wordkeeper.di.DialogScope
import com.akhbulatov.wordkeeper.presentation.global.mvvm.ViewModelModule
import dagger.Subcomponent

@DialogScope
@Subcomponent(modules = [SelectWordCategoryModule::class, ViewModelModule::class])
interface SelectWordCategoryComponent {
    fun inject(dialog: SelectWordCategoryDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SelectWordCategoryComponent
    }
}
