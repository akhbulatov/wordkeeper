package com.akhbulatov.wordkeeper.domain.global.repositories

import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import kotlinx.coroutines.flow.Flow

interface WordCategoryRepository {
    fun getWordCategories(): Flow<List<WordCategory>>
}