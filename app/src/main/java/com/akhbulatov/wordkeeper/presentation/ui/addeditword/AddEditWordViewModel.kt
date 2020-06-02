package com.akhbulatov.wordkeeper.presentation.ui.addeditword

import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.domain.word.WordInteractor
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AddEditWordViewModel @Inject constructor(
    private val wordInteractor: WordInteractor,
    private val wordCategoryInteractor: WordCategoryInteractor
) : BaseViewModel() {

    fun getWordCategories(): List<String> =
        runBlocking {
            wordCategoryInteractor.getWordCategories()
                .first()
                .map { it.name }
        }

    fun onAddWordClicked(name: String, translation: String, category: String) {
        val word = Word(
            name = name,
            translation = translation,
            datetime = System.currentTimeMillis(),
            category = category
        )
        viewModelScope.launch {
            wordInteractor.addWord(word)
        }
    }

    fun onEditWordClicked(id: Long, name: String, translation: String, category: String) {
        val word = Word(
            id = id,
            name = name,
            translation = translation,
            datetime = System.currentTimeMillis(),
            category = category
        )
        viewModelScope.launch {
            wordInteractor.editWord(word)
        }
    }
}
