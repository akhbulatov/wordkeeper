package com.akhbulatov.wordkeeper.domain.word

import com.akhbulatov.wordkeeper.domain.word.models.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getWords(): Flow<List<Word>>
    fun getWordsByCategory(category: String): Flow<List<Word>>

    suspend fun addWord(word: Word)
    suspend fun editWord(word: Word)
    suspend fun deleteWords(words: List<Word>)

    fun getWordSortMode(): Word.SortMode
    fun setWordSortMode(mode: Word.SortMode)
}
