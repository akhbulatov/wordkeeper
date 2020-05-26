package com.akhbulatov.wordkeeper.presentation.ui.sortword

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment

class SortWordDialog : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()
        val sortMode = args.getSerializable(ARG_SORT_MODE)!! as Word.SortMode
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.words_action_sort)
            .setSingleChoiceItems(R.array.sort_words, sortMode.ordinal) { _, which ->
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

        fun newInstance(sortMode: Word.SortMode) = SortWordDialog().apply {
            arguments = bundleOf(ARG_SORT_MODE to sortMode)
        }
    }
}