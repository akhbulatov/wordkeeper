package com.akhbulatov.wordkeeper.presentation.ui.main

import com.akhbulatov.wordkeeper.di.ActivityScope
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
@ActivityScope
interface MainComponent {
    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
}
