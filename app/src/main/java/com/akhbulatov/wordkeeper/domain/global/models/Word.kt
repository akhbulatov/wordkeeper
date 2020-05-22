package com.akhbulatov.wordkeeper.domain.global.models

data class Word(
    val id: Long = 0,
    val name: String,
    val translation: String,
    val datetime: Long,
    val category: String
) {

    enum class SortMode {
        NAME,
        DATETIME
    }
}