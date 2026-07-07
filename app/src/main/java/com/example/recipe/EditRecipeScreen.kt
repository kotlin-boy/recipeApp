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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun EditRecipeScreen(
    navController: NavController,
    recipeId: Int
){

    val focusManager = LocalFocusManager.current

    //ViewModel
    val viewModel: EditRecipeViewModel = hiltViewModel()


    LaunchedEffect(recipeId){
        viewModel.loadRecipe(recipeId)
    }

    /////////////////ここから画面//////////////////////
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


        Text("レシピ編集")

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
                    viewModel.updateRecipe()
                    navController.popBackStack()
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
