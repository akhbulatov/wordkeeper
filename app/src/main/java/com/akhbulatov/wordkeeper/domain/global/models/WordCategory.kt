package com.akhbulatov.wordkeeper.domain.global.models

data class WordCategory(
    val id: Long = 0,
    val name: String,
    val words: List<Word>
)