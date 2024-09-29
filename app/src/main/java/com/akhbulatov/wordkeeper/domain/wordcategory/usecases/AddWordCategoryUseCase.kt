package com.akhbulatov.wordkeeper.domain.wordcategory.usecases

import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryRepository
import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory

class AddWordCategoryUseCase(
    private val wordCategoryRepository: WordCategoryRepository
) {

    suspend operator fun invoke(wordCategory: WordCategory) {
        wordCategoryRepository.addWordCategory(wordCategory)
    }
}
