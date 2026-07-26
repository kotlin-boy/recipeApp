package com.example.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//ジャンルドロップダウン
@Composable
fun GenreDropDown(
    selectedGenre: RecipeGenre?,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onGenreSelected: (RecipeGenre?) -> Unit,
    showAllItem: Boolean = false
) {
    Box {

        Button(
            onClick = onExpandClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                selectedGenre?.displayName ?: "すべて"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ){
            if (showAllItem) {
                DropdownMenuItem(
                    text = {
                        Text("すべて")
                    },
                    onClick = {
                        onGenreSelected(null)
                    }
                )
            }

            RecipeGenre.entries.forEach { genre ->
                DropdownMenuItem(
                    text = {
                        Text(genre.displayName)
                    },
                    onClick = {
                        onGenreSelected(genre)
                    }
                )
            }
        }
    }
}