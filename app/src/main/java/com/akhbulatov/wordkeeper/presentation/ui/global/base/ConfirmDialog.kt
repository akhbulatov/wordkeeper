package com.akhbulatov.wordkeeper.presentation.ui.global.base

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConfirmDialog : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = requireArguments()
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(args.getInt(ARG_TITLE))
            .setMessage(args.getInt(ARG_MESSAGE))
            .setPositiveButton(args.getInt(ARG_POSITIVE_BUTTON)) { _, _ ->
                setFragmentResult(REQUEST_OK, bundleOf())
            }
            .setNegativeButton(args.getInt(ARG_NEGATIVE_BUTTON)) { _, _ -> dismiss() }
            .create()
    }

    companion object {
        const val REQUEST_OK = "request_ok"
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_POSITIVE_BUTTON = "positive_button"
        private const val ARG_NEGATIVE_BUTTON = "negative_button"

        fun newInstance(
            @StringRes title: Int,
            @StringRes message: Int,
            @StringRes positiveButton: Int = android.R.string.ok,
            @StringRes negativeButton: Int = android.R.string.cancel
        ) = ConfirmDialog().apply {
            arguments = bundleOf(
                ARG_TITLE to title,
                ARG_MESSAGE to message,
                ARG_POSITIVE_BUTTON to positiveButton,
                ARG_NEGATIVE_BUTTON to negativeButton
            )
        }
    }
}
