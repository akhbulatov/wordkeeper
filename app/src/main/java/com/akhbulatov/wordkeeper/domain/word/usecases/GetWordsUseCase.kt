package com.akhbulatov.wordkeeper.domain.word.usecases

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.models.Word
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWordsUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {

    operator fun invoke(): Flow<List<Word>> =
        wordRepository.getWords()
}
