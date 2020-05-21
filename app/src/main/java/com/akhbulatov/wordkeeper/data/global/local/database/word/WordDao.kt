package com.akhbulatov.wordkeeper.data.global.local.database.word

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words ORDER BY name")
    fun getAllSortByName(): Flow<List<WordDbModel>>

    @Query("SELECT * FROM words ORDER BY datetime DESC")
    fun getAllSortByDescDatetime(): Flow<List<WordDbModel>>
}