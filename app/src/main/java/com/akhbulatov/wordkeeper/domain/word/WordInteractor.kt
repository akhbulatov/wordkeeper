package com.akhbulatov.wordkeeper.domain.word

import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.domain.global.repositories.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WordInteractor @Inject constructor(
    private val wordRepository: WordRepository
) {

    fun getWords(): Flow<List<Word>> =
        wordRepository.getWords()

    fun getWordsByCategory(category: String): Flow<List<Word>> =
        wordRepository.getWordsByCategory(category)

    suspend fun addWord(word: Word) =
        wordRepository.addWord(word)

    suspend fun editWord(word: Word) =
        wordRepository.editWord(word)

    suspend fun deleteWords(words: List<Word>) =
        wordRepository.deleteWords(words)

    suspend fun searchWords(query: String, source: List<Word>): List<Word> =
        withContext(Dispatchers.IO) {
            source.filter {
                it.name.startsWith(query, ignoreCase = true)
            }
        }

    var wordSortMode: Word.SortMode
        get() = wordRepository.wordSortMode
        set(value) {
            wordRepository.wordSortMode = value
        }
}
