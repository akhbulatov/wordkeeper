package com.akhbulatov.wordkeeper.data.wordcategory

import com.akhbulatov.wordkeeper.core.database.word.WordDao
import com.akhbulatov.wordkeeper.core.database.wordcategory.WordCategoryDao
import com.akhbulatov.wordkeeper.data.word.WordDatabaseMapper
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.domain.global.repositories.WordCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordCategoryRepositoryImpl @Inject constructor(
    private val wordCategoryDao: WordCategoryDao,
    private val wordDao: WordDao,
    private val wordCategoryDatabaseMapper: WordCategoryDatabaseMapper,
    private val wordDatabaseMapper: WordDatabaseMapper
) : WordCategoryRepository {

    override fun getWordCategories(): Flow<List<WordCategory>> =
        wordCategoryDao.getAllWordCategories()
            .map {
                it.map { category ->
                    val dbWords = wordDao.getWordsByCategory(category.name).firstOrNull() ?: emptyList()
                    val words = dbWords.map { dbWord -> wordDatabaseMapper.mapFrom(dbWord) }
                    wordCategoryDatabaseMapper.mapFrom(category, words)
                }
            }

    override suspend fun addWordCategory(wordCategory: WordCategory) {
        val dbModel = wordCategoryDatabaseMapper.mapTo(wordCategory)
        wordCategoryDao.insertWordCategory(dbModel)
    }

    override suspend fun editWordCategory(wordCategory: WordCategory) {
        val dbModel = wordCategoryDatabaseMapper.mapTo(wordCategory)
        wordCategoryDao.updateWordCategory(dbModel)
    }

    override suspend fun deleteWordCategoryWithWords(wordCategory: WordCategory) {
        val dbWords = wordCategory.words.map { wordDatabaseMapper.mapTo(it) }
        val dbWordCategory = wordCategoryDatabaseMapper.mapTo(wordCategory)
        wordDao.deleteWords(dbWords)
        wordCategoryDao.deleteWordCategory(dbWordCategory)
    }
}
