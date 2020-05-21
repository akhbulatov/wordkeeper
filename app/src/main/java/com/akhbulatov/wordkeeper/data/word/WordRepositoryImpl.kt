package com.akhbulatov.wordkeeper.data.word

import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDao
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.domain.global.repositories.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao,
    private val wordDatabaseMapper: WordDatabaseMapper
) : WordRepository {

    override fun getWords(sortMode: Word.SortMode): Flow<List<Word>> =
        when (sortMode) {
            Word.SortMode.NAME -> wordDao.getAllSortByName()
            Word.SortMode.DATETIME -> wordDao.getAllSortByDescDatetime()
        }
            .map { it.map { word -> wordDatabaseMapper.mapFrom(word) } }
}