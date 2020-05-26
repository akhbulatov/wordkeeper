package com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.DialogAddEditWordCategoryBinding
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges

class AddEditWordCategoryDialog : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val wordCategory: WordCategoryUiModel? = arguments?.getParcelable(ARG_WORD_CATEGORY)
        val titleId = if (wordCategory == null) {
            R.string.title_new_category
        } else {
            R.string.title_rename_category
        }
        val positiveTextId = if (wordCategory == null) {
            R.string.category_editor_action_add
        } else {
            R.string.category_editor_action_rename
        }

        val inflater = requireActivity().layoutInflater
        val binding = DialogAddEditWordCategoryBinding.inflate(inflater, null, false)

        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setTitle(titleId)
            .setPositiveButton(positiveTextId) { _, _ ->
                val name = binding.nameEditText.text.toString()
                val result = bundleOf(RESULT_WORD_CATEGORY to name)
                setFragmentResult(REQUEST_WORD_CATEGORY, result)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
            .create()

        wordCategory?.let {
            binding.nameEditText.setText(it.name)
        }

        dialog.setOnShowListener {
            binding.nameEditText.textChanges(emitImmediately = true)
                .onEach { dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = it.isNotBlank() }
                .launchIn(lifecycleScope)
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    companion object {
        const val REQUEST_WORD_CATEGORY = "request_word_category"
        const val RESULT_WORD_CATEGORY = "result_word_category"
        private const val ARG_WORD_CATEGORY = "word_category"

        fun newInstance(wordCategory: WordCategoryUiModel? = null) = AddEditWordCategoryDialog().apply {
            arguments = bundleOf(ARG_WORD_CATEGORY to wordCategory)
        }
    }
}