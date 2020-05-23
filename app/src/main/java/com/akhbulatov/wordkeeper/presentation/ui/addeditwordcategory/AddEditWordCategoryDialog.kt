package com.akhbulatov.wordkeeper.presentation.ui.addeditwordcategory

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.DialogAddEditWordCategoryBinding
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordCategoryUiModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

class AddEditWordCategoryDialog : BaseDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AddEditWordCategoryViewModel by viewModels { viewModelFactory }

    private var wordCategory: WordCategoryUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .addEditWordCategoryComponentFactory()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        wordCategory = arguments?.getParcelable(ARG_WORD_CATEGORY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

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

        val binding = DialogAddEditWordCategoryBinding.inflate(inflater, null, false)
        builder.setView(binding.root)
            .setTitle(titleId)
            .setPositiveButton(positiveTextId) { _: DialogInterface?, _: Int ->
                val name = binding.nameEditText.text.toString()

                if (wordCategory == null) {
                    viewModel.onAddWordCategoryClicked(name)
                } else {
                    viewModel.onEditWordCategoryClicked(wordCategory!!.id, name)
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }

        wordCategory?.let { binding.nameEditText.setText(it.name) }

        val dialog = builder.create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setOnShowListener {
            binding.nameEditText.textChanges(emitImmediately = true)
                .map { it.isNotBlank() }
                .onEach { dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = it }
                .launchIn(lifecycleScope)
        }
        return dialog
    }

    companion object {
        private const val ARG_WORD_CATEGORY = "word_category"

        @JvmStatic
        fun newInstance(wordCategory: WordCategoryUiModel? = null) =
            AddEditWordCategoryDialog().apply {
                arguments = bundleOf(ARG_WORD_CATEGORY to wordCategory)
            }
    }
}