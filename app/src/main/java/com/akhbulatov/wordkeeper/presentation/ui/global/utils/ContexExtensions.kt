package com.akhbulatov.wordkeeper.presentation.ui.global.utils

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackbar(@StringRes textId: Int) {
    Snackbar.make(requireView(), textId, Snackbar.LENGTH_SHORT).show()
}