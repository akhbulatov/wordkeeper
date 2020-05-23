package com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory

import androidx.lifecycle.viewModelScope
import com.akhbulatov.wordkeeper.domain.global.models.WordCategory
import com.akhbulatov.wordkeeper.domain.wordcategory.WordCategoryInteractor
import com.akhbulatov.wordkeeper.presentation.global.mvvm.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditWordCategoryViewModel @Inject constructor(
    private val wordCategoryInteractor: WordCategoryInteractor
) : BaseViewModel() {

    fun onAddWordCategoryClicked(name: String) {
        val wordCategory = WordCategory(name = name)
        viewModelScope.launch {
            wordCategoryInteractor.addWordCategory(wordCategory)
        }
    }

    fun onEditWordCategoryClicked(id: Long, name: String) {
        val wordCategory = WordCategory(
            id = id,
            name = name
        )
        viewModelScope.launch {
            wordCategoryInteractor.editWordCategory(wordCategory)
        }
    }

    override fun onBackPressed() {}
}