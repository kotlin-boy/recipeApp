package com.example.recipe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.ui.Alignment

@Composable
fun SelectionHeader(
    selectedCount: Int,
    onCancel: () -> Unit,
    onFavoriteClick: () -> Unit,
    onUnFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onCancel
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "選択解除"
            )
        }

        Text(
            text = "${selectedCount}件選択中",
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onFavoriteClick
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "お気に入り追加"
            )
        }

        IconButton(
            onClick = onUnFavoriteClick
        ) {
            Icon(
                imageVector = Icons.Default.StarBorder,
                contentDescription = "お気に入り解除"
            )
        }

        IconButton(
            onClick = onDeleteClick
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "削除"
            )
        }
    }
}