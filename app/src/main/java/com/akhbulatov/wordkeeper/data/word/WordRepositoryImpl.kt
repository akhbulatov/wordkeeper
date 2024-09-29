package com.akhbulatov.wordkeeper.data.word

import com.akhbulatov.wordkeeper.core.preferences.AppPreferences
import com.akhbulatov.wordkeeper.data.word.database.WordDao
import com.akhbulatov.wordkeeper.data.word.database.WordDatabaseMapper
import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordRepositoryImpl(
    private val wordDao: WordDao,
    private val appPreferences: AppPreferences,
    private val wordDatabaseMapper: WordDatabaseMapper
) : WordRepository {

    override fun getWords(): Flow<List<Word>> {
        val wordSortMode = appPreferences.wordSortMode
        return when (wordSortMode) {
            Word.SortMode.NAME -> wordDao.getAllWordsSortedByName()
            Word.SortMode.LAST_MODIFIED -> wordDao.getAllWordsSortedByDescDatetime()
        }
            .map { it.map { word -> wordDatabaseMapper.mapFrom(word) } }
    }

    override fun getWordsByCategory(category: String): Flow<List<Word>> =
        wordDao.getWordsByCategory(category)
            .map { it.map { word -> wordDatabaseMapper.mapFrom(word) } }

    override suspend fun addWord(word: Word) {
        val dbModel = wordDatabaseMapper.mapTo(word)
        wordDao.insetWord(dbModel)
    }

    override suspend fun editWord(word: Word) {
        val dbModel = wordDatabaseMapper.mapTo(word)
        wordDao.updateWord(dbModel)
    }

    override suspend fun deleteWords(words: List<Word>) {
        val dbModels = words.map { wordDatabaseMapper.mapTo(it) }
        wordDao.deleteWords(dbModels)
    }

    override fun getWordSortMode(): Word.SortMode {
        return appPreferences.wordSortMode
    }

    override fun setWordSortMode(mode: Word.SortMode) {
        appPreferences.wordSortMode = mode
    }
}
