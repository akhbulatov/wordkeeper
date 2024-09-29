package com.akhbulatov.wordkeeper.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router

class NavigationFactory {

    private val cicerone: Cicerone<Router> = Cicerone.create()
    val router: Router = cicerone.router
    val navigatorHolder: NavigatorHolder = cicerone.getNavigatorHolder()
}
