package com.akhbulatov.wordkeeper.data.wordcategory

import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDao
import com.akhbulatov.wordkeeper.data.global.local.database.wordcategory.WordCategoryDao
import com.akhbulatov.wordkeeper.data.global.local.database.wordcategory.WordCategoryDbModel
import com.akhbulatov.wordkeeper.data.word.WordDatabaseMapper
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.domain.global.repositories.WordCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject

class WordCategoryRepositoryImpl @Inject constructor(
    private val wordCategoryDao: WordCategoryDao,
    private val wordDao: WordDao,
    private val wordCategoryDatabaseMapper: WordCategoryDatabaseMapper,
    private val wordDatabaseMapper: WordDatabaseMapper
) : WordCategoryRepository {

    override fun getWordCategories(): Flow<List<WordCategory>> =
        wordCategoryDao.getAllWordCategories()
            .onEmpty { // todo not working
                val dbModel = WordCategoryDbModel("Main")
                wordCategoryDao.insertWordCategory(dbModel)
            }
            .map {
                if (it.isEmpty()) { // todo
                    val dbModel = WordCategoryDbModel("Main")
                    wordCategoryDao.insertWordCategory(dbModel)
                }

                it.map { wordCategory ->
                    val dbWords = wordDao.getWordsByCategory(wordCategory.name)
                    val words = dbWords.map { dbWord -> wordDatabaseMapper.mapFrom(dbWord) }
                    wordCategoryDatabaseMapper.mapFrom(wordCategory, words)
                }
            }
}