package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import java.time.YearMonth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipe.Red
import java.time.DayOfWeek
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    //現在の年月
    val currentMonth by viewModel.currentMonth.collectAsState()

    //指定年月の食事記録
    val monthMealRecords by viewModel.monthMealRecords.collectAsState()

    //その月の日数
    val days = (1..currentMonth.lengthOfMonth()).map { day ->
        currentMonth.atDay(day)
    }

    // 月の1日
    val firstDay = currentMonth.atDay(1)

    // 月初めに必要な空白セル数
    val emptyDays = firstDay.dayOfWeek.value % 7

    //曜日リスト
    val weekDays = listOf(
        "日", "月", "火", "水", "木", "金", "土"
    )

    // 選択中の日付
    val selectedDate by viewModel.selectedDate.collectAsState()

    // 選択した日に食べたレシピ
    val mealRecords by viewModel.mealRecords.collectAsState()

    // 登録されている全レシピ
    val recipes by viewModel.recipes.collectAsState()

    //レシピ登録用ダイアログ
    var showRecipeDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RecipeButton(
                    text = "レシピを登録",
                    onClick = {
                        showRecipeDialog = true
                    },
                    modifier = Modifier.weight(1f)
                )

                RecipeButton(
                    text = "戻る",
                    containerColor = Red,
                    contentColor = White,
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 6.dp)
        ) {
            //カレンダー
            RecipeCalendar(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                monthMealRecords = monthMealRecords,
                onPreviousMonth = viewModel::previousMonth,
                onNextMonth = viewModel::nextMonth,
                onDateClick = viewModel::selectDate
            )

            Text(
                text = "選択日：$selectedDate",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "この日に食べたレシピ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(mealRecords) { _, mealRecord ->
                    Text(
                        text = "・${mealRecord.recipeName}",
                        fontSize = 26.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    //レシピ登録ダイアログ
    if (showRecipeDialog) {
        CommonDialog(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            onDismissRequest = {
                showRecipeDialog = false
            },
            buttons = {

                NormalHeader(
                    searchKeyword = viewModel.searchKeyword,
                    onSearchKeywordChange = viewModel::updateSearchKeyword,
                    selectedGenre = viewModel.selectedGenre,
                    onGenreSelected = viewModel::updateSelectedGenre,
                    selectedSort = viewModel.sortOrder,
                    onSortSelected = viewModel::updateSortOrder,
                    favoriteOnly = viewModel.favoriteOnly,
                    onFavoriteClick = viewModel::toggleFavoriteFilter,
                    showImage = viewModel.showImage,
                    onShowImageClick = viewModel::toggleShowImage
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                ) {
                    items(
                        items = recipes,
                        key = { it.id }
                    ) { recipe ->

                        RecipeCard(
                            recipe = recipe,
                            isSelected = false,
                            isSelectionMode = false,
                            onClick = {
                                viewModel.addMealRecord(recipe.id)
                                showRecipeDialog = false
                            },
                            onLongClick = {},
                            showImage = viewModel.showImage
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                RecipeButton(
                    text = "キャンセル",
                    onClick = {
                        showRecipeDialog = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Red,
                    contentColor = White
                )
            }
        )
    }
}