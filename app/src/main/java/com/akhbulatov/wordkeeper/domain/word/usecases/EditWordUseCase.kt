package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word

class EditWordUseCase(
    private val wordRepository: WordRepository
) {

    suspend operator fun invoke(word: Word) {
        wordRepository.editWord(word)
    }
}
