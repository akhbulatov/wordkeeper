package com.akhbulatov.wordkeeper.data.wordcategory.database

import com.akhbulatov.wordkeeper.data.wordcategory.database.models.WordCategoryDbModel
import com.akhbulatov.wordkeeper.domain.word.models.Word
import com.akhbulatov.wordkeeper.domain.wordcategory.models.WordCategory
import javax.inject.Inject

class WordCategoryDatabaseMapper @Inject constructor() {

    fun mapTo(model: WordCategory): WordCategoryDbModel =
        model.let {
            WordCategoryDbModel(
                name = it.name
            ).also { db ->
                db.id = it.id
            }
        }

    fun mapFrom(model: WordCategoryDbModel, words: List<Word>): WordCategory =
        model.let {
            WordCategory(
                id = it.id,
                name = it.name,
                words = words
            )
        }
}
