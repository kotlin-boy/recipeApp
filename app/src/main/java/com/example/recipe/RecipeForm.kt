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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RecipeForm(
    recipeName: String,
    onRecipeNameChange: (String) -> Unit,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onDismissRequest: () -> Unit,
    selectedGenre: RecipeGenre?,
    onGenreSelected: (RecipeGenre?) -> Unit,
    memo: String,
    onMemoValueChange: (String) -> Unit,
    ingredients: List<String>,
    onIngredientValueChange: (Int, String) -> Unit,
    onRemoveIngredient: (Int) -> Unit,
    onAddIngredient: () -> Unit,
    steps: List<String>,
    onStepValueChange: (Int, String) -> Unit,
    onRemoveStep: (Int) -> Unit,
    onAddStep: () -> Unit,
    isFavorite: Boolean,
    onFavoriteChange: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
){

    var showBackDialog by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = recipeName,
        onValueChange = onRecipeNameChange,
        label = {
            Text("レシピ名")
        }
    )

    //ジャンル指定
    GenreDropDown(
        selectedGenre = selectedGenre,
        expanded = expanded,
        onExpandClick = onExpandClick,
        onDismissRequest = onDismissRequest,
        onGenreSelected = onGenreSelected
    )

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

    Row {
        Button(
            onClick = onSaveClick
        ) {
            Text("保存")
        }

        Button(
            onClick = {
                showBackDialog = true
            }
        ) {
            Text("戻る")
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onFavoriteChange
        ) {
            Icon(
                imageVector =
                    if (isFavorite)
                        Icons.Default.Star
                    else
                        Icons.Default.StarBorder,
                contentDescription = "お気に入り"
            )
        }
    }

    if (showBackDialog) {
        AlertDialog(
            onDismissRequest = {
                showBackDialog = false
            },
            title = {
                Text("確認")
            },
            text = {
                Text("入力内容は保存されません。\n戻りますか？")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBackDialog = false
                        onBackClick()
                    }
                ) {
                    Text("戻る")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showBackDialog = false
                    }
                ) {
                    Text("キャンセル")
                }
            }
        )
    }
}
