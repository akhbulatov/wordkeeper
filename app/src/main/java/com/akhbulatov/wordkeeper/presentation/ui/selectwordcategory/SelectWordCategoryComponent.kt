package com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory

import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.core.ui.mvvm.ViewModelModule
import com.akhbulatov.wordkeeper.di.DialogScope
import dagger.Subcomponent

@DialogScope
@Subcomponent(modules = [SelectWordCategoryModule::class, ViewModelModule::class])
interface SelectWordCategoryComponent {
    fun inject(dialog: SelectWordCategoryDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SelectWordCategoryComponent
    }

    companion object {
        fun create(): SelectWordCategoryComponent = App.appComponent
            .selectWordCategoryComponentFactory()
            .create()
    }
}
