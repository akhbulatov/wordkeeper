package com.akhbulatov.wordkeeper.presentation.ui.deletewordcategory

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel
import javax.inject.Inject

class DeleteWordCategoryDialog : BaseDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: DeleteWordCategoryViewModel by viewModels { viewModelFactory }

    private lateinit var wordCategory: WordCategoryUiModel

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .deleteWordCategoryComponentFactory()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        wordCategory = requireArguments().getParcelable(ARG_WORD_CATEGORY)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        return builder.setTitle(R.string.category_delete_title)
            .setMessage(R.string.category_delete_message)
            .setPositiveButton(R.string.category_action_delete) { _: DialogInterface?, _: Int ->
                viewModel.onDeleteWordCategoryWithWordsClicked(wordCategory)
            }
            .setNegativeButton(android.R.string.cancel) { _: DialogInterface, _: Int -> dismiss() }
            .create()
    }

    companion object {
        private const val ARG_WORD_CATEGORY = "word_category"

        @JvmStatic
        fun newInstance(wordCategory: WordCategoryUiModel) =
            DeleteWordCategoryDialog().apply {
                arguments = bundleOf(ARG_WORD_CATEGORY to wordCategory)
            }
    }
}