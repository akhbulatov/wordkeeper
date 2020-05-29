package com.akhbulatov.wordkeeper.domain.wordcategory

import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.domain.global.repositories.WordCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WordCategoryInteractor @Inject constructor(
    private val wordCategoryRepository: WordCategoryRepository
) {

    fun getWordCategories(): Flow<List<WordCategory>> =
        wordCategoryRepository.getWordCategories()

    suspend fun addWordCategory(wordCategory: WordCategory) =
        wordCategoryRepository.addWordCategory(wordCategory)

    suspend fun editWordCategory(wordCategory: WordCategory) =
        wordCategoryRepository.editWordCategory(wordCategory)

    suspend fun deleteWordCategoryWithWords(wordCategory: WordCategory) =
        wordCategoryRepository.deleteWordCategoryWithWords(wordCategory)

    suspend fun searchWordCategories(query: String, source: List<WordCategory>): List<WordCategory> =
        withContext(Dispatchers.IO) {
            source.filter {
                it.name.startsWith(query, ignoreCase = true)
            }
        }
}
