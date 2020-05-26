package com.akhbulatov.wordkeeper.presentation.ui.selectwordcategory

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment
import javax.inject.Inject

class SelectWordCategoryDialog : BaseDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SelectWordCategoryViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .selectWordCategoryComponentFactory()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()
        val categories = viewModel.getWordCategories()
        return AlertDialog.Builder(requireContext())
            .setTitle(args.getInt(ARG_TITLE))
            .setItems(categories) { _, which ->
                val result = bundleOf(RESULT_SELECT_WORD_CATEGORY to categories[which])
                setFragmentResult(REQUEST_SELECT_WORD_CATEGORY, result)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
            .create()
    }

    companion object {
        const val REQUEST_SELECT_WORD_CATEGORY = "request_select_word_category"
        const val RESULT_SELECT_WORD_CATEGORY = "result_select_word_category"
        private const val ARG_TITLE = "title"

        fun newInstance(@StringRes title: Int) = SelectWordCategoryDialog().apply {
            arguments = bundleOf(ARG_TITLE to title)
        }
    }
}