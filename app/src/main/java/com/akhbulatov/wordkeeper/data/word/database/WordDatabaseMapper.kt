package com.akhbulatov.wordkeeper.data.word.database

import com.akhbulatov.wordkeeper.data.word.database.models.WordDbModel
import com.akhbulatov.wordkeeper.domain.word.models.Word
import javax.inject.Inject

class WordDatabaseMapper @Inject constructor() {

    fun mapTo(model: Word): WordDbModel =
        model.let {
            WordDbModel(
                name = it.name,
                translation = it.translation,
                datetime = it.datetime,
                category = it.category
            ).also { db ->
                db.id = it.id
            }
        }

    fun mapFrom(model: WordDbModel): Word =
        model.let {
            Word(
                id = it.id,
                name = it.name,
                translation = it.translation,
                datetime = it.datetime,
                category = it.category
            )
        }
}
