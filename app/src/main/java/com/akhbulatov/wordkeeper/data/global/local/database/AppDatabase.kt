package com.akhbulatov.wordkeeper.data.global.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDao
import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDbModel
import com.akhbulatov.wordkeeper.data.global.local.database.wordcategory.WordCategoryDao
import com.akhbulatov.wordkeeper.data.global.local.database.wordcategory.WordCategoryDbModel
import com.akhbulatov.wordkeeper.database.DatabaseContract

@Database(
    entities = [
        WordDbModel::class,
        WordCategoryDbModel::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun wordCategoryDao(): WordCategoryDao

    companion object {
        val MIGRATION_0_1 = object : Migration(0, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(DatabaseContract.SQL_CREATE_WORD_ENTRIES)
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(DatabaseContract.SQL_WORD_ADD_COLUMN_DATETIME)
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(DatabaseContract.SQL_WORD_ADD_COLUMN_CATEGORY)
                database.execSQL(DatabaseContract.SQL_CREATE_CATEGORY_ENTRIES)
                database.execSQL("INSERT INTO categories (_id, name) VALUES (0, 'Main')")
            }
        }
    }
}
