package com.akhbulatov.wordkeeper.presentation.ui.global.utils

import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

fun FragmentActivity.getSupportActionBar(): ActionBar? =
    (this as AppCompatActivity).supportActionBar

fun Fragment.showSnackbar(@StringRes textId: Int) {
    Snackbar.make(requireView(), textId, Snackbar.LENGTH_SHORT).show()
}