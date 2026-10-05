package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.DatePicker
import java.time.Instant
import java.time.ZoneId
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
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

    //カレンダーダイアログ
    var showCalendarDialog by remember {
        mutableStateOf(false)
    }


    //開幕で指定レシピ取得
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    val recipe = viewModel.recipe

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(6.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                if (viewModel.recipe != null) {

                    val recipe = viewModel.recipe!!

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "レシピ名（${recipe.genre.displayName}）",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = viewModel::toggleFavorite
                        ) {
                            Icon(
                                imageVector =
                                    if (recipe.isFavorite)
                                        Icons.Default.Star
                                    else
                                        Icons.Default.StarBorder,
                                contentDescription = "お気に入り",
                                tint =
                                    if (recipe.isFavorite)
                                        Yellow
                                    else
                                        Color.Black
                            )
                        }
                    }

                    Text(
                        text = recipe.name,
                        fontSize = 26.sp,
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
                } else {

                    Text(
                        text = "読み込み中..."
                    )

                }
            }
        }

        if (recipe != null) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                RecipeButton(
                    text = "編集",
                    onClick = {
                        navController.navigate(
                            "edit_recipe/${recipe.id}"
                        )
                    }
                )

                Spacer(modifier = Modifier.width(4.dp))

                RecipeButton(
                    text = "食事記録登録",
                    onClick = {
                        showCalendarDialog = true
                    },
                )

                Spacer(modifier = Modifier.width(4.dp))

                RecipeButton(
                    text = "戻る",
                    onClick = {
                        navController.popBackStack()
                    },
                    containerColor = Red,
                    contentColor = White
                )

                Spacer(modifier = Modifier.weight(1f))

                RecipeButton(
                    text = "削除",
                    onClick = {
                        showDeleteDialog = true
                    },
                    containerColor = Red,
                    contentColor = White
                )
            }
        }
    }

    if (showDeleteDialog) {
        CommonDialog(
            title = "削除の確認",
            message = {
                Text("このレシピを削除しますか？")
            },
            onDismissRequest = {
                showDeleteDialog = false
            },
            buttons = {
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
        )
    }

    if (showCalendarDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = {
                showCalendarDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .toString()

                            viewModel.addMealRecord(date)
                        }

                        showCalendarDialog = false
                    }
                ) {
                    Text("登録")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCalendarDialog = false
                    }
                ) {
                    Text("キャンセル")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}
