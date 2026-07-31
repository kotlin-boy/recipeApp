package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun RecipeViewScreen(
    navController: NavController
) {
    //hilt
    val viewModel: RecipeViewModel = hiltViewModel()

    //flow
    val recipes by viewModel.recipes.collectAsState()

    //削除ダイアログ
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(8.dp)
    ) {

        if (viewModel.isSelectionMode) {
            SelectionHeader(
                selectedCount = viewModel.selectedIds.size,
                onCancel = viewModel::exitSelectionMode,
                onFavoriteClick = viewModel::favoriteSelected,
                onUnFavoriteClick = viewModel::unFavoriteSelected,
                onDeleteClick = {
                    showDeleteDialog = true
                }
            )
        } else {
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
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ){
            items(
                items = recipes,
                key = { it.id }
            )  { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        isSelected = viewModel.isSelected(recipe.id),
                        isSelectionMode = viewModel.isSelectionMode,
                        onClick = {
                            if (viewModel.isSelectionMode) {
                                viewModel.toggleSelection(recipe.id)
                            } else {
                                navController.navigate("recipe_detail/${recipe.id}")
                            }
                        },
                        onLongClick = {
                            viewModel.enterSelectionMode(recipe.id)
                        },
                        showImage = viewModel.showImage
                    )
            }
        }

        //削除ダイアログ
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                title = {
                    Text("確認")
                },
                text = {
                    Text("${viewModel.selectedIds.size}件のレシピを削除しますか？")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteSelected()
                            showDeleteDialog = false
                        }
                    ) {
                        Text("削除")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ) {
                        Text("キャンセル")
                    }
                }
            )
        }
    }
}