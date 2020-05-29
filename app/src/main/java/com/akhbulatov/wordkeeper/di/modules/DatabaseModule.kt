package com.akhbulatov.wordkeeper.di.modules

import android.content.Context
import androidx.room.Room
import com.akhbulatov.wordkeeper.data.global.local.database.AppDatabase
import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDao
import com.akhbulatov.wordkeeper.data.global.local.database.wordcategory.WordCategoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DatabaseModule {
    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "wordkeeper.db")
                .addMigrations(
                    AppDatabase.MIGRATION_0_1,
                    AppDatabase.MIGRATION_1_2,
                    AppDatabase.MIGRATION_2_3
                )
                .build()

        @Provides
        @Singleton
        fun provideWordDao(appDatabase: AppDatabase): WordDao = appDatabase.wordDao()

        @Provides
        @Singleton
        fun provideWordCategoryDao(appDatabase: AppDatabase): WordCategoryDao = appDatabase.wordCategoryDao()
    }
}
