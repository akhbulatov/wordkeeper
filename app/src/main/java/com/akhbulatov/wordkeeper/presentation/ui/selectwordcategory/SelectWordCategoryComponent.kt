package com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory

import com.akhbulatov.wordkeeper.di.DialogScope
import dagger.Subcomponent

@Subcomponent(modules = [SelectWordCategoryModule::class])
@DialogScope
interface SelectWordCategoryComponent {
    fun inject(dialog: SelectWordCategoryDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SelectWordCategoryComponent
    }
}