package com.akhbulatov.wordkeeper.presentation.ui.global.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes colorId: Int): Int = ContextCompat.getColor(this, colorId)
