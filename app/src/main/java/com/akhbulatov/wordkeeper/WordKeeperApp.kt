package com.akhbulatov.wordkeeper

import android.app.Application
import com.akhbulatov.wordkeeper.di.AppFactory

class WordKeeperApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: WordKeeperApp

        val appFactory: AppFactory by lazy {
            AppFactory(
                appContext = instance.applicationContext
            )
        }
    }
}
