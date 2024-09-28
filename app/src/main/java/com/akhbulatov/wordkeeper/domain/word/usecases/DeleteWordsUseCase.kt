package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word
import javax.inject.Inject

class DeleteWordsUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {

    suspend operator fun invoke(words: List<Word>) {
        wordRepository.deleteWords(words)
    }
}
