package com.example.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
    memo: String,
    onMemoValueChange: (String) -> Unit,
    ingredients: List<String>,
    onIngredientValueChange: (Int, String) -> Unit,
    onRemoveIngredient: (Int) -> Unit,
    onAddIngredient: () -> Unit,
    steps: List<String>,
    onStepValueChange: (Int, String) -> Unit,
    onRemoveStep: (Int) -> Unit,
    onAddStep: () -> Unit
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

    //材料の記載・追加
    Text("材料")
    ingredients.forEachIndexed { index, ingredient ->
        Row {
            //材料欄
            OutlinedTextField(
                value = ingredient,
                onValueChange = {
                    onIngredientValueChange(index, it)
                },
                label = {
                    Text("材料${index + 1}")
                }
            )
            //削除ボタン
            Button(
                onClick = {
                    onRemoveIngredient(index)
                }
            ) {
                Text("×")
            }
        }
    }

    //材料リストの追加
    Button(
        onClick = {
            onAddIngredient()
        }
    ) {
        Text("＋")
    }

    //手順の記載・追加
    steps.forEachIndexed { index, step ->

        Row {
            //手順欄
            OutlinedTextField(
                value = step,
                onValueChange = {
                    onStepValueChange(index, it)
                },
                label = {
                    Text("手順${index + 1}")
                }
            )
            //削除ボタン
            Button(
                onClick = {
                    onRemoveStep(index)
                }
            ) {
                Text("×")
            }
        }
    }

    //追加ボタン
    Button(
        onClick = {
            onAddStep()
        }
    ) {
        Text("＋")
    }

    //メモゾーン
    OutlinedTextField(
        value = memo,
        onValueChange = onMemoValueChange,
        label = {
            Text("メモ")
        },
        minLines = 5
    )
}
