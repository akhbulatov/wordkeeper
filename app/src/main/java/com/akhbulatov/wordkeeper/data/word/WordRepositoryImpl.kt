package com.akhbulatov.wordkeeper.data.word

import com.akhbulatov.wordkeeper.core.database.word.WordDao
import com.akhbulatov.wordkeeper.core.preferences.word.WordPreferences
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.domain.global.repositories.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao,
    private val wordPreferences: WordPreferences,
    private val wordDatabaseMapper: WordDatabaseMapper
) : WordRepository {

    override fun getWords(): Flow<List<Word>> {
        val wordSortMode = wordPreferences.wordSortMode
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

    override var wordSortMode: Word.SortMode
        get() = wordPreferences.wordSortMode
        set(value) {
            wordPreferences.wordSortMode = value
        }
}
