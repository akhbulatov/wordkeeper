package com.akhbulatov.wordkeeper.core.ui.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme {
        content()
    }
}
