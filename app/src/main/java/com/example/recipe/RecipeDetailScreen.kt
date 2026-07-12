package com.example.recipe

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
import androidx.compose.ui.platform.LocalContext
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

                //非同期処理
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.deleteRecipe()
                            navController.popBackStack()
                        }
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
    }
}