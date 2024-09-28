package com.akhbulatov.wordkeeper.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.core.database.word.WordDao
import com.akhbulatov.wordkeeper.core.database.word.WordDbModel
import com.akhbulatov.wordkeeper.core.database.wordcategory.WordCategoryDao
import com.akhbulatov.wordkeeper.core.database.wordcategory.WordCategoryDbModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "wordkeeper.db")
                .fallbackToDestructiveMigrationOnDowngrade()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            prePopulateWordCategory(context, getInstance(context).wordCategoryDao())
                        }
                    }
                })
                .build()

        suspend fun prePopulateWordCategory(context: Context, wordCategoryDao: WordCategoryDao) {
            val defaultCategory = context.getString(R.string.word_categories_default)
            val wordCategory = WordCategoryDbModel(defaultCategory)
            wordCategoryDao.insertWordCategory(wordCategory)
        }
    }
}
