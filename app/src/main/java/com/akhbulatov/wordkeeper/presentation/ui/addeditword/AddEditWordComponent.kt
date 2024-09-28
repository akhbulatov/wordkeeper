package com.akhbulatov.wordkeeper.presentation.ui.addeditword

import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.core.ui.mvvm.ViewModelModule
import com.akhbulatov.wordkeeper.di.DialogScope
import dagger.Subcomponent

@DialogScope
@Subcomponent(modules = [AddEditWordModule::class, ViewModelModule::class])
interface AddEditWordComponent {
    fun inject(dialog: AddEditWordDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditWordComponent
    }

    companion object {
        fun create(): AddEditWordComponent = App.appComponent
            .addEditWordComponentFactory()
            .create()
    }
}
