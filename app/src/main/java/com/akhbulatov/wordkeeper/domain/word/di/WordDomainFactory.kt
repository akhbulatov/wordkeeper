package com.akhbulatov.wordkeeper.domain.word.di

import com.akhbulatov.wordkeeper.domain.word.WordRepository
import com.akhbulatov.wordkeeper.domain.word.usecases.AddWordUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.DeleteWordsUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.EditWordUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.GetWordSortModeUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.GetWordsByCategoryUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.GetWordsUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.SetWordSortModeUseCase

class WordDomainFactory(
    wordRepository: WordRepository
) {

    val getWordsUseCase = GetWordsUseCase(
        wordRepository = wordRepository
    )
    val getWordsByCategoryUseCase = GetWordsByCategoryUseCase(
        wordRepository = wordRepository
    )
    val addWordUseCase = AddWordUseCase(
        wordRepository = wordRepository
    )
    val editWordUseCase = EditWordUseCase(
        wordRepository = wordRepository
    )
    val deleteWordsUseCase = DeleteWordsUseCase(
        wordRepository = wordRepository
    )

    val getWordSortModeUseCase = GetWordSortModeUseCase(
        wordRepository = wordRepository
    )
    val setWordSortModeUseCase = SetWordSortModeUseCase(
        wordRepository = wordRepository
    )
}
