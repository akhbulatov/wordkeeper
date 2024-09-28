package com.akhbulatov.wordkeeper.core.ui.models

import android.os.Parcelable
import com.akhbulatov.wordkeeper.domain.word.models.Word
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordUiModel(
    val id: Long,
    val name: String,
    val translation: String,
    val datetime: Long,
    val category: String
) : Parcelable

fun Word.toUiModel() = WordUiModel(
    id = id,
    name = name,
    translation = translation,
    datetime = datetime,
    category = category
)
