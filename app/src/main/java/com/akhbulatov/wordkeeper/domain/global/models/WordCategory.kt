package com.akhbulatov.wordkeeper.domain.global.models

import com.akhbulatov.wordkeeper.domain.word.models.Word

data class WordCategory(
    val id: Long = 0,
    val name: String,
    val words: List<Word> = listOf()
)
