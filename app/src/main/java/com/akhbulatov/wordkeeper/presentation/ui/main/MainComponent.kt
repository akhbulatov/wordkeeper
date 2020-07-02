package com.akhbulatov.wordkeeper.presentation.ui.main

import com.akhbulatov.wordkeeper.di.ActivityScope
import com.akhbulatov.wordkeeper.presentation.global.mvvm.ViewModelModule
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [MainModule::class, ViewModelModule::class])
interface MainComponent {
    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
}
