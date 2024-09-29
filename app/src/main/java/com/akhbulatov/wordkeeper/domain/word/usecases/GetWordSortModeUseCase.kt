package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word

class GetWordSortModeUseCase(
    private val wordRepository: WordRepository
) {

    operator fun invoke(): Word.SortMode =
        wordRepository.getWordSortMode()
}
