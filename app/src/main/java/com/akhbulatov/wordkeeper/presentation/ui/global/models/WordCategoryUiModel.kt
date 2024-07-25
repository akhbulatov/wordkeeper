package com.akhbulatov.wordkeeper.presentation.ui.global.models

import android.os.Parcelable
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordCategoryUiModel(
    val id: Long,
    val name: String
) : Parcelable

fun WordCategory.toUiModel() = WordCategoryUiModel(
    id = id,
    name = name
)
