package com.akhbulatov.wordkeeper.data.word

import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDao
import com.akhbulatov.wordkeeper.data.global.local.preferences.word.WordPreferences
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
            Word.SortMode.NAME -> wordDao.getAllSortByName()
            Word.SortMode.LAST_MODIFIED -> wordDao.getAllSortByDescDatetime()
        }
            .map { it.map { word -> wordDatabaseMapper.mapFrom(word) } }
    }

    override suspend fun addWord(word: Word) {
        val dbModel = wordDatabaseMapper.mapTo(word)
        wordDao.add(dbModel)
    }

    override suspend fun editWord(word: Word) {
        val dbModel = wordDatabaseMapper.mapTo(word)
        wordDao.edit(dbModel)
    }

    override suspend fun deleteWords(words: List<Word>) {
        val dbModels = words.map { wordDatabaseMapper.mapTo(it) }
        wordDao.delete(dbModels)
    }

    override var wordSortMode: Word.SortMode
        get() = wordPreferences.wordSortMode
        set(value) {
            wordPreferences.wordSortMode = value
        }
}