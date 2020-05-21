package com.akhbulatov.wordkeeper.di

import android.content.Context
import com.akhbulatov.wordkeeper.di.modules.AppModule
import com.akhbulatov.wordkeeper.di.modules.DataModule
import com.akhbulatov.wordkeeper.di.modules.DatabaseModule
import com.akhbulatov.wordkeeper.di.modules.NavigationModule
import com.akhbulatov.wordkeeper.di.modules.ViewModelModule
import com.akhbulatov.wordkeeper.presentation.ui.main.MainComponent
import com.akhbulatov.wordkeeper.presentation.ui.words.WordsComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        DataModule::class,
        NavigationModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface AppComponent {
    fun mainComponentFactory(): MainComponent.Factory
    fun wordsComponentFactory(): WordsComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}