package com.example.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.navigation.NavController

@Composable
fun RecipeViewScreen(
    navController: NavController
) {

    val recipes = listOf(
        Recipe(
            id = 1,
            name = "親子丼",
            genre = RecipeGenre.MAIN,
            ingredients = emptyList(),
            steps = emptyList(),
            memo = ""
        ),
        Recipe(
            id = 2,
            name = "ポテトサラダ",
            genre = RecipeGenre.SALAD,
            ingredients = emptyList(),
            steps = emptyList(),
            memo = ""
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {

        Text("レシピ一覧")

        LazyColumn {
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