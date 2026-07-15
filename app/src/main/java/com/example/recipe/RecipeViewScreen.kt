package com.example.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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


@Composable
fun RecipeViewScreen(
    navController: NavController
) {
    //hilt
    val viewModel: RecipeViewModel = hiltViewModel()

    //flow
    val recipes by viewModel.recipes.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
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

        //ジャンルフィルター
        GenreDropDown(
            selectedGenre = viewModel.selectedGenre,
            expanded = expanded,
            onExpandClick = {
                expanded = true
            },
            onDismissRequest = {
                expanded = false
            },
            onGenreSelected = { genre ->
                viewModel.updateSelectedGenre(genre)
                expanded = false
            },
            showAllItem = true
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ){
            items(recipes) { recipe ->

                Card(
                    onClick = {
                        navController.navigate(
                            "recipe_detail/${recipe.id}"
                        )
                    },
                    modifier = Modifier.padding(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = recipe.name
                        )

                        Text(
                            text = recipe.genre.displayName
                        )
                    }
                }
            }
        }
    }
}