package com.example.recipe

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddRecipeScreen(
    navController: NavController
) {
    //フォーカス解除用
    val focusManager = LocalFocusManager.current

    //アプリ状態
    val context = LocalContext.current

    //ViewModel
    val viewModel: AddRecipeViewModel = hiltViewModel()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    //戻る確認ダイアログ用
    var showBackDialog by remember {
        mutableStateOf(false)
    }

    //画像選択用
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.updateImageUri(it.toString())
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        val message = viewModel.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        viewModel.clearErrorMessage()
    }

////////////////////////UIゾーン////////////////////////
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
                .clickable {
                    focusManager.clearFocus()
                }
        ) {
            Text(
                text = "レシピ追加",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

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
                isFavorite = viewModel.isFavorite,
                onFavoriteChange = {
                    viewModel.toggleFavorite()
                },
                onSaveClick = {
                    if (viewModel.saveRecipe()) {
                        navController.popBackStack()
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                hasChanges = viewModel.hasChanges(),
                onImageSelectClick = {
                    imagePickerLauncher.launch("image/*")
                },
                onRotateImageClick = {
                    if (viewModel.imageUri.isNotBlank()) {
                        val newUri = ImageUtils.rotateImage(
                            context,
                            viewModel.imageUri
                        )
                        viewModel.updateImageUri(newUri)
                    }
                },
                onDeleteImageClick = {
                    viewModel.removeImage()
                },
                imageUri = viewModel.imageUri,
            )
        }
    }
}