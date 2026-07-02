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

//ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun AddRecipeScreen(
    navController: NavController
) {
    //フォーカス解除用
    val focusManager = LocalFocusManager.current

    //ViewModel
    val viewModel: AddRecipeViewModel = viewModel()

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
            recipeName = viewModel.recipeName,
            onRecipeNameChange = viewModel::updateRecipeName,

            //ジャンルドロップダウンの状態変数とイベント関数を渡す
            expanded = viewModel.expanded,
            onExpandClick = viewModel::expendGenre,
            onDismissRequest = viewModel::dismissGenre,
            selectedGenre = viewModel.selectedGenre,
            onGenreSelected = viewModel::updateGenre,

            //材料の状態リストとイベント関数を渡す
            ingredients = viewModel.ingredients,
            onIngredientValueChange = viewModel::updateIngredient,
            onRemoveIngredient = viewModel::removeIngredient,
            onAddIngredient = viewModel::addIngredient,

            //手順の状態リストとイベント関数を渡す
            steps = viewModel.steps,
            onStepValueChange = viewModel::updateStep,
            onRemoveStep = viewModel::removeStep,
            onAddStep = viewModel::addStep,

            //メモの状態変数とイベント関数を渡す
            memo = viewModel.memo,
            onMemoValueChange = viewModel::updateMemo,
        )

        //保存処理
        Row{
            Button(
                onClick = {

                    val recipe = Recipe(
                        id = 0,
                        name = viewModel.recipeName,
                        genre = viewModel.selectedGenre,
                        ingredients = viewModel.ingredients.toList(),
                        steps = viewModel.steps.toList(),
                        memo = viewModel.memo
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