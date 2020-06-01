package com.akhbulatov.wordkeeper.data.global.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDao
import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDbModel
import com.akhbulatov.wordkeeper.data.global.local.database.wordcategory.WordCategoryDao
import com.akhbulatov.wordkeeper.data.global.local.database.wordcategory.WordCategoryDbModel

@Database(
    entities = [
        WordDbModel::class,
        WordCategoryDbModel::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun wordCategoryDao(): WordCategoryDao
}
