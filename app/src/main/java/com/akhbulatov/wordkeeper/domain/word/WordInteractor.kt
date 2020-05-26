package com.akhbulatov.wordkeeper.domain.word

import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.domain.global.repositories.WordRepository
import kotlinx.coroutines.flow.Flow
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

    var wordSortMode: Word.SortMode
        get() = wordRepository.wordSortMode
        set(value) {
            wordRepository.wordSortMode = value
        }
}