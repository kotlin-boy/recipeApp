package com.example.recipe

import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailScreen(
    navController: NavController,
    recipeId: Int
) {
    //hilt
    val viewModel: RecipeDatailViewModel = hiltViewModel()

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    //開幕で指定レシピ取得
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        if (viewModel.recipe != null) {

            val recipe = viewModel.recipe!!

            Text(
                text = recipe.name
            )

            Text(
                text = recipe.genre.displayName
            )

            Text("材料")
            recipe.ingredients.forEach { ingredient ->

                Text(
                    text = "・$ingredient"
                )

            }

            Text("手順")
            recipe.steps.forEachIndexed { index, step ->

                Text(
                    text = "${index + 1}. $step"
                )

            }

            Text("メモ")

            Text(
                text = recipe.memo
            )

            Row {

                Button(
                    onClick = {
                        //編集画面へ
                        navController.navigate(
                            "edit_recipe/${recipe.id}"
                        )
                    }
                ) {
                    Text("編集")
                }

                Button(
                    onClick = {
                        showDeleteDialog = true
                    }
                ) {
                    Text("削除")
                }
            }

        } else {

            Text(
                text = "読み込み中..."
            )

        }

        if (showDeleteDialog) {
            AlertDialog(
                confirmButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                showDeleteDialog = false
                                viewModel.deleteRecipe()
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Text("削除")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ){
                        Text("キャンセル")
                    }
                },
                onDismissRequest = {
                    showDeleteDialog = false
                },
                title = {
                    Text("削除の確認")
                },
                text = {
                    Text("このレシピを削除しますか？")
                }
            )
        }
    }
}
