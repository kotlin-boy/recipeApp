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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue

@Composable
fun RecipeViewScreen(
    navController: NavController
) {
    //アプリの現在状態
    val context = LocalContext.current
    //DBの作成(状態保持、createはstatic関数)
    val database = remember {
        RecipeDatabase.create(context)
    }
    //データ操作セット
    val recipeDao = database.recipeDao()

    //データ保持用リスト
    var recipes by remember {
        mutableStateOf<List<Recipe>>(
            emptyList()
        )
    }

    //画面表示時に全件レシピ取得
    LaunchedEffect(Unit) {
        recipes = recipeDao.getAll()
        println(recipes)
    }

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
                        println("送るID = ${recipe.id}")
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