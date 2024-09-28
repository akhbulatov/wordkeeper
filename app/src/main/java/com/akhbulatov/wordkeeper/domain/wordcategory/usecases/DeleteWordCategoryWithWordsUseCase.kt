package com.akhbulatov.wordkeeper.domain.wordcategory.usecases

import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryRepository
import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory
import javax.inject.Inject

class DeleteWordCategoryWithWordsUseCase @Inject constructor(
    private val wordCategoryRepository: WordCategoryRepository
) {

    suspend operator fun invoke(wordCategory: WordCategory) {
        wordCategoryRepository.deleteWordCategoryWithWords(wordCategory)
    }
}
