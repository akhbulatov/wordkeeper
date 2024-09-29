package com.akhbulatov.wordkeeper.domain.wordcategory.usecases

import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryRepository
import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory

class EditWordCategoryUseCase(
    private val wordCategoryRepository: WordCategoryRepository
) {

    suspend operator fun invoke(wordCategory: WordCategory) {
        wordCategoryRepository.editWordCategory(wordCategory)
    }
}
