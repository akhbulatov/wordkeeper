package com.akhbulatov.wordkeeper.domain.wordcategory.usecases

import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryRepository
import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory

class DeleteWordCategoryWithWordsUseCase(
    private val wordCategoryRepository: WordCategoryRepository
) {

    suspend operator fun invoke(wordCategory: WordCategory) {
        wordCategoryRepository.deleteWordCategoryWithWords(wordCategory)
    }
}
