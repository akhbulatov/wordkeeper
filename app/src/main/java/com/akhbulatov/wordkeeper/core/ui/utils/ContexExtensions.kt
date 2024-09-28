package com.akhbulatov.wordkeeper.core.ui.utils

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.requireCompatActivity(): AppCompatActivity = requireActivity() as AppCompatActivity

fun Fragment.showSnackbar(@StringRes textId: Int) {
    Snackbar.make(requireView(), textId, Snackbar.LENGTH_SHORT).show()
}
