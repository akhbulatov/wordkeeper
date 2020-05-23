package com.akhbulatov.wordkeeper.presentation.ui.global.models

import android.os.Parcelable
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WordCategoryUiModel(
    val id: Long,
    val name: String,
    val words: List<WordUiModel>
) : Parcelable

fun WordCategory.toUiModel() = WordCategoryUiModel(
    id = id,
    name = name,
    words = words.map { it.toUiModel() }
)

fun WordCategoryUiModel.toDomainModel() = WordCategory(
    id = id,
    name = name,
    words = words.map { it.toDomainModel() }
)