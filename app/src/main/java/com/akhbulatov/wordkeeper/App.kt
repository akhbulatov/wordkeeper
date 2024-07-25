package com.akhbulatov.wordkeeper

import android.app.Application
import com.akhbulatov.wordkeeper.di.AppComponent
import com.akhbulatov.wordkeeper.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
