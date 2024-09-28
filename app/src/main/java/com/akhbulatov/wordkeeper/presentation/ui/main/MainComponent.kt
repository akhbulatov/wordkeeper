package com.akhbulatov.wordkeeper.presentation.ui.main

import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.core.ui.mvvm.ViewModelModule
import com.akhbulatov.wordkeeper.di.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [MainModule::class, ViewModelModule::class])
interface MainComponent {
    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    companion object {
        fun create(): MainComponent = App.appComponent
            .mainComponentFactory()
            .create()
    }
}
