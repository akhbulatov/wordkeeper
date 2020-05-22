package com.akhbulatov.wordkeeper.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.akhbulatov.wordkeeper.data.global.local.preferences.word.WordPreferences
import com.akhbulatov.wordkeeper.data.global.local.preferences.word.WordPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class PreferencesModule {
    @Binds
    @Singleton
    abstract fun bindWordPreferences(prefs: WordPreferencesImpl): WordPreferences

    companion object {
        @Provides
        @Singleton
        fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences("wordkeeper.prefs", Context.MODE_PRIVATE)
    }
}