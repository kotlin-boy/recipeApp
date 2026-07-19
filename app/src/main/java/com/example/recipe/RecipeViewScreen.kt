package com.example.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun RecipeViewScreen(
    navController: NavController
) {
    //hilt
    val viewModel: RecipeViewModel = hiltViewModel()

    //flow
    val recipes by viewModel.recipes.collectAsState()

    var genreExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(8.dp)
    ) {

        Text("レシピ一覧")

        OutlinedTextField(

            value = viewModel.searchKeyword,
            onValueChange = viewModel::updateSearchKeyword,
            label = {
                Text("レシピ名で検索")
            },
            modifier = Modifier.fillMaxWidth(),

            trailingIcon = {
                if (viewModel.searchKeyword.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            viewModel.updateSearchKeyword("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "検索文字をクリア"
                        )
                    }
                }
            }
        )

        Row {
            //ジャンルフィルター
            GenreDropDown(
                selectedGenre = viewModel.selectedGenre,
                expanded = genreExpanded,
                onExpandClick = {
                    genreExpanded = true
                    sortExpanded = false
                },
                onDismissRequest = {
                    genreExpanded = false
                },
                onGenreSelected = { genre ->
                    viewModel.updateSelectedGenre(genre)
                    genreExpanded = false
                },
                showAllItem = true
            )

            //ソート選択
            SortDropDown(
                selectedSort = viewModel.sortOrder,
                expanded = sortExpanded,
                onExpandClick = {
                    sortExpanded = true
                    genreExpanded = false
                },
                onDismissRequest = {
                    sortExpanded = false
                },
                onSortSelected = { sort ->
                    viewModel.updateSortOrder(sort)
                    sortExpanded = false
                }
            )

            //お気に入りフィルター
            IconButton(
                onClick = {
                    viewModel.toggleFavoriteFilter()
                }
            ) {
                Icon(
                    imageVector =
                        if (viewModel.favoriteOnly)
                            Icons.Default.Star
                        else
                            Icons.Default.StarBorder,
                    contentDescription = "お気に入り"
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ){
            items(recipes) { recipe ->

                Card(
                    onClick = {
                        navController.navigate(
                            "recipe_detail/${recipe.id}"
                        )
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {

                            Text(
                                text = recipe.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = recipe.genre.displayName
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.toggleFavorite(recipe)
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (recipe.isFavorite)
                                        Icons.Default.Star
                                    else
                                        Icons.Default.StarBorder,
                                contentDescription = "お気に入り"
                            )
                        }
                    }
                }
            }
        }
    }
}