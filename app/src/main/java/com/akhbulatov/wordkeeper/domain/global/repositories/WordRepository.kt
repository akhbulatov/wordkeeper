package com.akhbulatov.wordkeeper.domain.global.repositories

import com.akhbulatov.wordkeeper.domain.global.models.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getWords(sortMode: Word.SortMode): Flow<List<Word>>

    suspend fun addWord(word: Word)
    suspend fun editWord(word: Word)
    suspend fun deleteWords(words: List<Word>)
}