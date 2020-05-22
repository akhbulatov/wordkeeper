package com.akhbulatov.wordkeeper.presentation.ui.sortword

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.domain.global.models.Word
import com.akhbulatov.wordkeeper.presentation.global.mvvm.ViewModelFactory
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment
import javax.inject.Inject

class SortWordDialog : BaseDialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SortWordViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .sortWordComponentFactory()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val sortMode = viewModel.getWordSortMode().ordinal
        return builder.setTitle(R.string.words_action_sort)
            .setSingleChoiceItems(R.array.sort_words, sortMode) { _: DialogInterface, which: Int ->
                val selectedSortMode = Word.SortMode.toEnumSortMode(which)
                viewModel.onSortWordClicked(selectedSortMode)
                setFragmentResult(REQUEST_SORT_MODE, bundleOf())
                dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { _: DialogInterface, _: Int -> dismiss() }
            .create()
    }

    companion object {
        const val REQUEST_SORT_MODE = "request_sort_mode"
    }
}