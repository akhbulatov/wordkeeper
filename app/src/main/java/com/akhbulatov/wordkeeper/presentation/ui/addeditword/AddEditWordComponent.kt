package com.akhbulatov.wordkeeper.presentation.ui.addeditword

import com.akhbulatov.wordkeeper.di.DialogScope
import dagger.Subcomponent

@Subcomponent(modules = [AddEditWordModule::class])
@DialogScope
interface AddEditWordComponent {
    fun inject(dialog: AddEditWordDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditWordComponent
    }
}