package com.akhbulatov.wordkeeper.data.global.local.database

import android.content.ContentValues
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDao
import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDbModel
import com.akhbulatov.wordkeeper.database.DatabaseContract

@Database(
    entities = [WordDbModel::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

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

                // Creates a default category that cannot be deleted
                database.insert(DatabaseContract.CategoryEntry.TABLE_NAME, OnConflictStrategy.REPLACE, createDefaultCategory())
            }
        }

        private fun createDefaultCategory(): ContentValues? {
//            val defaultCategory: String = mContext.getResources().getString(R.string.default_category)
            val defaultCategory: String = "Main" // todo
            val values = ContentValues()
            values.put(DatabaseContract.CategoryEntry.COLUMN_NAME, defaultCategory)
            return values
        }
    }
}