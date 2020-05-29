package com.akhbulatov.wordkeeper.presentation.ui.global.models

import android.os.Parcelable
import com.akhbulatov.wordkeeper.domain.global.models.Word
import kotlinx.android.parcel.Parcelize

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

fun WordUiModel.toDomainModel() = Word(
    id = id,
    name = name,
    translation = translation,
    datetime = datetime,
    category = category
)
