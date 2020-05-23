package com.akhbulatov.wordkeeper.presentation.ui.deletewordcategory

import com.akhbulatov.wordkeeper.di.DialogScope
import dagger.Subcomponent

@Subcomponent(modules = [DeleteWordCategoryModule::class])
@DialogScope
interface DeleteWordCategoryComponent {
    fun inject(dialog: DeleteWordCategoryDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): DeleteWordCategoryComponent
    }
}