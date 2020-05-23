package com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory

import com.akhbulatov.wordkeeper.di.DialogScope
import dagger.Subcomponent

@Subcomponent(modules = [AddEditWordCategoryModule::class])
@DialogScope
interface AddEditWordCategoryComponent {
    fun inject(dialog: AddEditWordCategoryDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditWordCategoryComponent
    }
}