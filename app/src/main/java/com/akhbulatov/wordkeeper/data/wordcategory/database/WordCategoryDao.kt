package com.akhbulatov.wordkeeper.data.wordcategory.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.akhbulatov.wordkeeper.data.wordcategory.database.models.WordCategoryDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WordCategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllWordCategories(): Flow<List<WordCategoryDbModel>>

    @Insert
    suspend fun insertWordCategory(model: WordCategoryDbModel)

    @Update
    suspend fun updateWordCategory(model: WordCategoryDbModel)

    @Delete
    suspend fun deleteWordCategory(model: WordCategoryDbModel)
}
