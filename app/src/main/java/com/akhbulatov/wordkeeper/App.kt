package com.akhbulatov.wordkeeper

import android.app.Application
import com.akhbulatov.wordkeeper.di.AppComponent
import com.akhbulatov.wordkeeper.di.DaggerAppComponent
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
        initStetho()
        initCrashlytics()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
            .core(
                CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build()
            )
            .build()
        Fabric.with(this, crashlyticsKit)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}