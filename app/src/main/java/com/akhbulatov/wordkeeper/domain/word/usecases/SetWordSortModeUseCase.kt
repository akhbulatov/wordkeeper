package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word

class SetWordSortModeUseCase(
    private val wordRepository: WordRepository
) {

    operator fun invoke(mode: Word.SortMode) {
        wordRepository.setWordSortMode(mode)
    }
}
