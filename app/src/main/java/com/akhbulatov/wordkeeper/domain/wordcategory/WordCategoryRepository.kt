package com.akhbulatov.wordkeeper.domain.wordcategory

import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory
import kotlinx.coroutines.flow.Flow

interface WordCategoryRepository {
    fun getWordCategories(): Flow<List<WordCategory>>

    suspend fun addWordCategory(wordCategory: WordCategory)
    suspend fun editWordCategory(wordCategory: WordCategory)
    suspend fun deleteWordCategoryWithWords(wordCategory: WordCategory)
}
