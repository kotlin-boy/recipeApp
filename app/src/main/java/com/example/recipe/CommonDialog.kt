package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CommonDialog(
    title: String? = null,
    message: @Composable (() -> Unit)? = null,
    buttons: @Composable ColumnScope.() -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,

        title = if (title != null) {
            {
                Text(title)
            }
        } else {
            null
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