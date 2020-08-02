package com.akhbulatov.wordkeeper.di.modules

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object NavigationModule {
    private val cicerone: Cicerone<Router> = Cicerone.create()

    @JvmStatic
    @Provides
    @Singleton
    fun provideRouter(): Router = cicerone.router

    @JvmStatic
    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder = cicerone.getNavigatorHolder()
}
