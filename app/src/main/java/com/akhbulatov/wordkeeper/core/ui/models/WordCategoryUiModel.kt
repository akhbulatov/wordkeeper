package com.akhbulatov.wordkeeper.core.ui.models

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
