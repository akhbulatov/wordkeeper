package com.akhbulatov.wordkeeper.presentation.ui.addeditword

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.akhbulatov.wordkeeper.App
import com.akhbulatov.wordkeeper.R
import com.akhbulatov.wordkeeper.databinding.DialogAddEditWordBinding
import com.akhbulatov.wordkeeper.presentation.ui.global.base.BaseDialogFragment
import com.akhbulatov.wordkeeper.presentation.ui.global.models.WordUiModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

class AddEditWordDialog : BaseDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddEditWordViewModel by viewModels { viewModelFactory }
    private var word: WordUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent
            .addEditWordComponentFactory()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        word = arguments?.getParcelable(ARG_WORD)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val titleId = if (word == null) R.string.add_edit_word_add_title else R.string.add_edit_word_edit_title
        val positiveTextId = if (word == null) R.string.add_edit_word_action_add else R.string.add_edit_word_action_edit

        val binding = DialogAddEditWordBinding.inflate(inflater, null, false)
        builder.setView(binding.root)
            .setTitle(titleId)
            .setPositiveButton(positiveTextId) { _, _ ->
                val name = binding.nameEditText.text.toString()
                val translation = binding.translationEditText.text.toString()
//                val category = binding.categoriesSpinner.selectedItem.toString()
                val category = "Main" // todo

                if (word == null) {
                    viewModel.onAddWordClicked(name, translation, category)
                } else {
                    viewModel.onEditWordClicked(word!!.id, name, translation, category)
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }

        val adapter = ArrayAdapter<CharSequence>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            arrayListOf()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoriesSpinner.adapter = adapter

        word?.let {
            binding.nameEditText.setText(it.name)
            binding.translationEditText.setText(it.translation)
        }

        val dialog = builder.create()
        // Shows the soft keyboard automatically
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setOnShowListener {
            combine(
                binding.nameEditText.textChanges(emitImmediately = true),
                binding.translationEditText.textChanges(emitImmediately = true)
            ) { name, translation -> name.isNotBlank() && translation.isNotBlank() }
                .onEach { dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = it }
                .launchIn(lifecycleScope)
        }
        return dialog
    }

    companion object {
        private const val ARG_WORD = "word"

        @JvmStatic
        fun newInstance(word: WordUiModel? = null) = AddEditWordDialog().apply {
            arguments = bundleOf(ARG_WORD to word)
        }
    }
}