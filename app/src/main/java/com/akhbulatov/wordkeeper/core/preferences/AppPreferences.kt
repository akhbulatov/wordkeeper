package com.akhbulatov.wordkeeper.core.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.akhbulatov.wordkeeper.domain.word.models.Word
import javax.inject.Inject

class AppPreferences @Inject constructor(
    private val sharedPrefs: SharedPreferences
) {

    var wordSortMode: Word.SortMode
        get() {
            val wordSortMode = sharedPrefs.getInt(PREF_WORD_SORT_MODE, 1)
            return Word.SortMode.toEnumSortMode(wordSortMode)
        }
        set(value) {
            val wordSortMode = value.ordinal
            sharedPrefs.edit { putInt(PREF_WORD_SORT_MODE, wordSortMode) }
        }

    companion object {
        private const val PREF_WORD_SORT_MODE = "PREF_SORT_MODE"
    }
}
