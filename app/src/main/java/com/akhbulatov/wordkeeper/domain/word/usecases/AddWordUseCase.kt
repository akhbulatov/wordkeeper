package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word

class AddWordUseCase(
    private val wordRepository: WordRepository
) {

    suspend operator fun invoke(word: Word) {
        wordRepository.addWord(word)
    }
}
