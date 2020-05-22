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
    fun getAllSortByName(): Flow<List<WordDbModel>>

    @Query("SELECT * FROM words ORDER BY datetime DESC")
    fun getAllSortByDescDatetime(): Flow<List<WordDbModel>>

    @Insert
    suspend fun add(word: WordDbModel)

    @Update
    suspend fun edit(word: WordDbModel)

    @Delete
    suspend fun delete(words: List<WordDbModel>)
}