package com.akhbulatov.wordkeeper.core.di

import android.content.SharedPreferences
import com.akhbulatov.wordkeeper.core.preferences.di.PreferencesFactory

class CoreFactory(
    private val sharedPrefs: SharedPreferences
) {

    val preferencesFactory: PreferencesFactory by lazy {
        PreferencesFactory(
            sharedPrefs = sharedPrefs
        )
    }
}
