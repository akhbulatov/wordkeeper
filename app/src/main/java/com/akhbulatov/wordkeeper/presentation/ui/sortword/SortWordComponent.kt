package com.akhbulatov.wordkeeper.presentation.ui.sortword

import com.akhbulatov.wordkeeper.di.DialogScope
import dagger.Subcomponent

@Subcomponent(modules = [SortWordModule::class])
@DialogScope
interface SortWordComponent {
    fun inject(dialog: SortWordDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SortWordComponent
    }
}