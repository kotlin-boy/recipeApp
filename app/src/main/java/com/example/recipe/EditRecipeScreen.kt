package com.example.recipe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun EditRecipeScreen(
    navController: NavController,
    recipeId: Int
){

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val database = remember {
        RecipeDatabase.create(context)
    }

    val recipeDao = database.recipeDao()

    var originalRecipe by remember {
        mutableStateOf<Recipe?>(null)
    }

    val scope = rememberCoroutineScope()


    //状態管理変数
    var recipeName by remember {
        mutableStateOf("")
    }

    var selectedGenre by remember {
        mutableStateOf(RecipeGenre.MAIN)
    }

    val ingredients = remember {
        mutableStateListOf("")
    }

    val steps = remember {
        mutableStateListOf("")
    }

    var memo by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(recipeId){
        originalRecipe = recipeDao.getRecipeById(recipeId)
        originalRecipe?.let {

            recipeName = it.name
            selectedGenre = it.genre

            ingredients.clear()
            ingredients.addAll(it.ingredients)

            steps.clear()
            steps.addAll(it.steps)

            memo = it.memo
        }
    }

    /////////////////ここから画面//////////////////////
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
            .padding(16.dp)
            .verticalScroll(
                rememberScrollState()
            )
            .clickable {
                focusManager.clearFocus()
            }
    ) {

        //レシピ名の記入
        Text("レシピ追加")
        OutlinedTextField(
            value = recipeName,
            onValueChange = {
                recipeName = it
            },
            label = {
                Text("レシピ名")
            }
        )

        //ジャンル指定
        Box {
            Button(
                onClick = {
                    expanded = true
                }
            ) {
                Text(selectedGenre.displayName)
            }
            //ドロップダウンの用意
            DropdownMenu(
                expanded = expanded,
                //フォーカス切れると閉じる
                onDismissRequest = {
                    expanded = false
                }
            ) {
                RecipeGenre.entries.forEach { genre ->
                    //ドロップダウンメニューの項目
                    DropdownMenuItem(
                        text = {
                            Text(genre.displayName)
                        },
                        onClick = {
                            selectedGenre = genre
                            //項目選択で閉じる
                            expanded = false
                        }
                    )
                }
            }
        }

        //材料の記載・追加
        Text("材料")
        ingredients.forEachIndexed { index, ingredient ->
            Row {
                //材料欄
                OutlinedTextField(
                    value = ingredient,
                    onValueChange = {
                        ingredients[index] = it
                    },
                    label = {
                        Text("材料${index + 1}")
                    }
                )
                //削除ボタン
                Button(
                    onClick = {
                        if (ingredients.size > 1) {
                            ingredients.removeAt(index)
                        }
                    }
                ) {
                    Text("×")
                }
            }
        }

        //材料リストの追加
        Button(
            onClick = {
                ingredients.add("")
            }
        ) {
            Text("＋")
        }

        //手順の記載・追加
        steps.forEachIndexed { index, step ->

            Row {
                //手順欄
                OutlinedTextField(
                    value = step,
                    onValueChange = {
                        steps[index] = it
                    },
                    label = {
                        Text("手順${index + 1}")
                    }
                )
                //削除ボタン
                Button(
                    onClick = {
                        if (steps.size > 1) {
                            steps.removeAt(index)
                        }
                    }
                ) {
                    Text("×")
                }
            }
        }

        //追加ボタン
        Button(
            onClick = {
                steps.add("")
            }
        ) {
            Text("＋")
        }


        //メモゾーン
        OutlinedTextField(
            value = memo,
            onValueChange = {
                memo = it
            },
            label = {
                Text("メモ")
            },
            minLines = 5
        )

        Button(
            onClick = {

                val recipe = Recipe(
                    id = originalRecipe!!.id,
                    name = recipeName,
                    genre = selectedGenre,
                    ingredients = ingredients.toList(),
                    steps = steps.toList(),
                    memo = memo
                )

                scope.launch {

                    try {

                        recipeDao.update(recipe)
                        println("保存成功")
                        navController.popBackStack()

                    } catch (e: Exception) {

                        println("保存失敗")

                    }
                }
            }
        ) {
            Text("保存")
        }

        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("戻る")
        }
    }
}
