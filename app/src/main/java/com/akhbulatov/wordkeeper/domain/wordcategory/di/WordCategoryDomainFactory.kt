package com.akhbulatov.wordkeeper.domain.wordcategory.di

import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryRepository
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.AddWordCategoryUseCase
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.DeleteWordCategoryWithWordsUseCase
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.EditWordCategoryUseCase
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.GetWordCategoriesUseCase

class WordCategoryDomainFactory(
    wordCategoryRepository: WordCategoryRepository
) {

    val getWordCategoriesUseCase = GetWordCategoriesUseCase(
        wordCategoryRepository = wordCategoryRepository
    )
    val addWordCategoryUseCase = AddWordCategoryUseCase(
        wordCategoryRepository = wordCategoryRepository
    )
    val editWordCategoryUseCase = EditWordCategoryUseCase(
        wordCategoryRepository = wordCategoryRepository
    )
    val deleteWordCategoryWithWordsUseCase = DeleteWordCategoryWithWordsUseCase(
        wordCategoryRepository = wordCategoryRepository
    )
}
