package com.akhbulatov.wordkeeper

import android.app.Application
import com.akhbulatov.wordkeeper.di.AppComponent
import com.akhbulatov.wordkeeper.di.DaggerAppComponent
import com.facebook.stetho.Stetho

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
        initStetho()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
