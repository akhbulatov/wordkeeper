package com.akhbulatov.wordkeeper.data.word

import com.akhbulatov.wordkeeper.data.global.local.database.word.WordDbModel
import com.akhbulatov.wordkeeper.domain.global.models.Word
import javax.inject.Inject

class WordDatabaseMapper @Inject constructor() {

    fun mapTo(model: Word): WordDbModel =
        model.let {
            WordDbModel(
                name = it.name,
                translation = it.translation,
                datetime = System.currentTimeMillis(),
                category = it.category
            )
        }

    fun mapFrom(model: WordDbModel): Word =
        model.let {
            Word(
                name = it.name,
                translation = it.translation,
                category = it.category
            )
        }
}