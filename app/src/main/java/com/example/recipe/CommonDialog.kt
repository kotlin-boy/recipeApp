package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CommonDialog(
    title: String,
    message: @Composable (() -> Unit)? = null,
    buttons: @Composable ColumnScope.() -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(title)
        },
        text = {
            Column {
                message?.invoke()
                buttons()
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}