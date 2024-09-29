package com.akhbulatov.wordkeeper.core.preferences.di

import android.content.SharedPreferences
import com.akhbulatov.wordkeeper.core.preferences.AppPreferences

class PreferencesFactory(
    private val sharedPrefs: SharedPreferences
) {

    val appPreferences: AppPreferences by lazy {
        AppPreferences(
            sharedPrefs = sharedPrefs
        )
    }
}
