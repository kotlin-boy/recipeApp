package com.example.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RecipeForm(
    recipeName: String,
    onRecipeNameChange: (String) -> Unit,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectedGenre: RecipeGenre,
    onGenreSelected: (RecipeGenre) -> Unit,
){
    OutlinedTextField(
        value = recipeName,
        onValueChange = onRecipeNameChange,
        label = {
            Text("レシピ名")
        }
    )

    //ジャンル指定
    Box {
        Button(
            onClick = onExpandClick
        ) {
            Text(selectedGenre.displayName)
        }
        //ドロップダウンの用意
        DropdownMenu(
            expanded = expanded,
            //フォーカス切れると閉じる
            onDismissRequest = onDismissRequest
        ) {
            RecipeGenre.entries.forEach { genre ->
                //ドロップダウンメニューの項目
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
