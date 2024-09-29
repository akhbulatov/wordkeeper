package com.akhbulatov.wordkeeper.domain.wordcategory.usecases

import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryRepository
import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory
import kotlinx.coroutines.flow.Flow

class GetWordCategoriesUseCase(
    private val wordCategoryRepository: WordCategoryRepository
) {

    operator fun invoke(): Flow<List<WordCategory>> =
        wordCategoryRepository.getWordCategories()
}
