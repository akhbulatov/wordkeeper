package com.akhbulatov.wordkeeper.data.global.local.database.word

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words ORDER BY name")
    fun getAllWordsSortedByName(): Flow<List<WordDbModel>>

    @Query("SELECT * FROM words ORDER BY datetime DESC")
    fun getAllWordsSortedByDescDatetime(): Flow<List<WordDbModel>>

    @Query("SELECT * FROM words WHERE category = :category")
    suspend fun getWordsByCategory(category: String): List<WordDbModel>

    @Insert
    suspend fun insetWord(word: WordDbModel)

    @Update
    suspend fun updateWord(word: WordDbModel)

    @Delete
    suspend fun deleteWord(words: List<WordDbModel>)
}