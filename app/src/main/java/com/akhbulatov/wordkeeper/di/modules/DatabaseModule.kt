package com.akhbulatov.wordkeeper.di.modules

import android.content.Context
import com.akhbulatov.wordkeeper.data.AppDatabase
import com.akhbulatov.wordkeeper.data.word.database.WordDao
import com.akhbulatov.wordkeeper.data.wordcategory.database.WordCategoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DatabaseModule {
    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)

        @Provides
        @Singleton
        fun provideWordDao(appDatabase: AppDatabase): WordDao = appDatabase.wordDao()

        @Provides
        @Singleton
        fun provideWordCategoryDao(appDatabase: AppDatabase): WordCategoryDao = appDatabase.wordCategoryDao()
    }
}
