package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddRecipeScreen(
    navController: NavController
) {

    var recipeName by remember {
        mutableStateOf("")
    }

    var memo by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {

        Text("レシピ追加")

        OutlinedTextField(
            value = recipeName,
            onValueChange = {
                recipeName = it
            },
            label = {
                Text("レシピ名")
            }
        )

        OutlinedTextField(
            value = memo,
            onValueChange = {
                memo = it
            },
            label = {
                Text("メモ")
            },
            minLines = 5
        )

        Button(
            onClick = {
                println(recipeName)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("保存")
        }

        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("戻る")
        }
    }
}