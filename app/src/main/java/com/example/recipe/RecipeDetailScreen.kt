package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(6.dp)
    ) {
        item {
            if (viewModel.recipe != null) {

                val recipe = viewModel.recipe!!

                Text(
                    text = "レシピ名（${recipe.genre.displayName}）",
                    fontSize = 26.sp,
                    color = Orange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )


                Text(
                    text = recipe.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = "材料",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = Orange,
                    fontWeight = FontWeight.Bold,
                )

                recipe.ingredients.forEach { ingredient ->

                    Text(
                        text = "・$ingredient",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                }

                Text(
                    text = "手順",
                    fontSize = 20.sp,
                    color = Orange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(bottom = 4.dp)
                )
                recipe.steps.forEachIndexed { index, step ->

                    Text(
                        text = "${index + 1}. $step",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                }

                Text(
                    text = "メモ",
                    fontSize = 20.sp,
                    color = Orange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = recipe.memo,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                if (recipe.imageUri.isNotBlank()) {
                    AsyncImage(
                        model = recipe.imageUri,
                        contentDescription = recipe.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                Row {
                    RecipeButton(
                        text = "編集",
                        onClick = {
                            //編集画面へ
                            navController.navigate(
                                "edit_recipe/${recipe.id}"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.width(4.dp))


                    RecipeButton(
                        text = "削除",
                        onClick = {
                            showDeleteDialog = true
                        },
                        containerColor = Red,
                        contentColor = White
                    )
                }

            } else {

                Text(
                    text = "読み込み中..."
                )

            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("削除の確認")
            },
            text = {
                Column {

                    Text("このレシピを削除しますか？")

                    Spacer(modifier = Modifier.height(16.dp))

                    RecipeButton(
                        text = "削除",
                        onClick = {
                            scope.launch {
                                showDeleteDialog = false
                                viewModel.deleteRecipe()
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Red,
                        contentColor = White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RecipeButton(
                        text = "キャンセル",
                        onClick = {
                            showDeleteDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}
