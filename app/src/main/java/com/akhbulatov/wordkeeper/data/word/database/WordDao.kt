package com.akhbulatov.wordkeeper.data.word.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.akhbulatov.wordkeeper.data.word.database.models.WordDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words ORDER BY name")
    fun getAllWordsSortedByName(): Flow<List<WordDbModel>>

    @Query("SELECT * FROM words ORDER BY datetime DESC")
    fun getAllWordsSortedByDescDatetime(): Flow<List<WordDbModel>>

    @Query("SELECT * FROM words WHERE category = :category")
    fun getWordsByCategory(category: String): Flow<List<WordDbModel>>

    @Insert
    suspend fun insetWord(word: WordDbModel)

    @Update
    suspend fun updateWord(word: WordDbModel)

    @Delete
    suspend fun deleteWords(words: List<WordDbModel>)
}
