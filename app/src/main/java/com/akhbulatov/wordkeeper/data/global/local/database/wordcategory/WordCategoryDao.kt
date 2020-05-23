package com.akhbulatov.wordkeeper.data.global.local.database.wordcategory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordCategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllWordCategories(): Flow<List<WordCategoryDbModel>>

    @Insert
    suspend fun insertWordCategory(model: WordCategoryDbModel)
}