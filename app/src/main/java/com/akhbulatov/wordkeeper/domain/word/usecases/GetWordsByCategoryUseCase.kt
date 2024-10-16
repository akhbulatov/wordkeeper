package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word
import kotlinx.coroutines.flow.Flow

class GetWordsByCategoryUseCase(
    private val wordRepository: WordRepository
) {

    operator fun invoke(category: String): Flow<List<Word>> =
        wordRepository.getWordsByCategory(category)
}
