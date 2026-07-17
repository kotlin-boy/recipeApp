package com.example.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

//ソートドロップダウン
@Composable
fun SortDropDown(
    selectedSort: SortOrder,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onSortSelected: (SortOrder) -> Unit,
) {
    Box {
        Button(
            onClick = onExpandClick
        ) {
            Text(selectedSort.displayName)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ){
            SortOrder.entries.forEach { sort ->
                DropdownMenuItem(
                    text = {
                        Text(sort.displayName)
                    },
                    onClick = {
                        onSortSelected(sort)
                    }
                )
            }
        }
    }
}