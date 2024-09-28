package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word
import javax.inject.Inject

class SetWordSortModeUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {

    operator fun invoke(mode: Word.SortMode) {
        wordRepository.setWordSortMode(mode)
    }
}
