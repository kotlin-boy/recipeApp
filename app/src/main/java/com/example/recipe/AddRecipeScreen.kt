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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


@Composable
fun AddRecipeScreen(
    navController: NavController
) {
    //フォーカス解除用
    val focusManager = LocalFocusManager.current

    //状態管理変数ゾーン
    //レシピ名
    var recipeName by remember {
        mutableStateOf("")
    }

    //ジャンル名
    var selectedGenre by remember {
        mutableStateOf(RecipeGenre.MAIN)
    }

    //プルダウンメニューのON・OFF
    var expanded by remember {
        mutableStateOf(false)
    }

    //材料のリスト
    val ingredients = remember {
        mutableStateListOf("")
    }

    //メモゾーン
    var memo by remember {
        mutableStateOf("")
    }

    //手順のリスト
    val steps = remember {
        mutableStateListOf("")
    }

    //非同期処理用
    val scope = rememberCoroutineScope()

    //現在のアプリ状態の保持
    val context = LocalContext.current

    //データベースの作成(static関数使用)
    val database = remember {
        RecipeDatabase.create(context)
    }

    val recipeDao = database.recipeDao()

////////////////////////UIゾーン////////////////////////

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
            .verticalScroll(
                rememberScrollState()
            )
            .clickable {
                focusManager.clearFocus()
            }
    ) {

        Text("レシピ追加")

        //レシピ用コンポーズ
        RecipeForm(
            //レシピ名の状態変数とイベント関数を渡す
            recipeName = recipeName,
            onRecipeNameChange = {
                recipeName = it
            },

            //ジャンルドロップダウンの状態変数とイベント関数を渡す
            expanded = expanded,
            onExpandClick = {expanded = true},
            onDismissRequest = {expanded = false},
            selectedGenre = selectedGenre,
            onGenreSelected = { genre ->
                selectedGenre = genre
                expanded = false
            },

            //材料の状態リストとイベント関数を渡す
            ingredients = ingredients,
            onIngredientValueChange = { index, value ->
                ingredients[index] = value
            },
            onRemoveIngredient = { index ->
                if (ingredients.size > 1) {
                    ingredients.removeAt(index)
                }
            },
            onAddIngredient = {
                ingredients.add("")
            },
            onAddStep = {
                steps.add("")
            },

            //手順の状態リストとイベント関数を渡す
            steps = steps,
            onStepValueChange = { index, value ->
                steps[index] = value
            },
            onRemoveStep = { index ->
                if (steps.size > 1)
                    steps.removeAt(index)
            },

            //メモの状態変数とイベント関数を渡す
            memo = memo,
            onMemoValueChange = {
                memo = it
            },
        )

        //保存処理
        Row{
            Button(
                onClick = {

                    val recipe = Recipe(
                        id = 0,
                        name = recipeName,
                        genre = selectedGenre,
                        ingredients = ingredients.toList(),
                        steps = steps.toList(),
                        memo = memo
                    )

                    scope.launch {

                        try {

                            recipeDao.insert(recipe)
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
}