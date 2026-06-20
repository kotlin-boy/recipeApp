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