package com.akhbulatov.wordkeeper.presentation.ui.sortword

import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.domain.word.WordInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import javax.inject.Inject

class SortWordViewModel @Inject constructor(
    private val wordInteractor: WordInteractor
) : BaseViewModel() {

    fun getWordSortMode(): Word.SortMode = wordInteractor.wordSortMode

    fun onSortWordClicked(sortMode: Word.SortMode) {
        wordInteractor.wordSortMode = sortMode
    }

    override fun onBackPressed() {}
}