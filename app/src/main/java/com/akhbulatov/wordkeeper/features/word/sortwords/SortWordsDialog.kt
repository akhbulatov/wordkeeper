package com.akhbulatov.wordkeeper.features.word.sortwords

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.core.ui.base.BaseDialogFragment
import com.akhbulatov.wordkeeper.domain.word.models.Word
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SortWordsDialog : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()
        val sortMode = args.getSerializable(ARG_SORT_MODE)!! as Word.SortMode
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.words_action_sort)
            .setSingleChoiceItems(R.array.sort_words_items, sortMode.ordinal) { _, which ->
                val selectedSortMode = Word.SortMode.toEnumSortMode(which)
                val result = bundleOf(RESULT_SORT_MODE to selectedSortMode)
                setFragmentResult(REQUEST_SORT_MODE, result)
                dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
            .create()
    }

    companion object {
        const val REQUEST_SORT_MODE = "request_sort_mode"
        const val RESULT_SORT_MODE = "request_sort_mode"
        private const val ARG_SORT_MODE = "sort_mode"

        fun newInstance(sortMode: Word.SortMode) = SortWordsDialog().apply {
            arguments = bundleOf(ARG_SORT_MODE to sortMode)
        }
    }
}
