package com.akhbulatov.wordkeeper.features.word.addeditword

import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.core.ui.mvvm.BaseViewModel
import com.akhbulatov.wordkeeper.domain.word.models.Word
import com.akhbulatov.wordkeeper.domain.word.usecases.AddWordUseCase
import com.akhbulatov.wordkeeper.domain.word.usecases.EditWordUseCase
import com.akhbulatov.wordkeeper.domain.wordcategory.usecases.GetWordCategoriesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AddEditWordViewModel @Inject constructor(
    private val addWordUseCase: AddWordUseCase,
    private val editWordUseCase: EditWordUseCase,
    private val getWordCategoriesUseCase: GetWordCategoriesUseCase
) : BaseViewModel() {

    fun getWordCategories(): List<String> =
        runBlocking {
            getWordCategoriesUseCase.invoke().first()
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
            addWordUseCase.invoke(word)
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
            editWordUseCase.invoke(word)
        }
    }
}
