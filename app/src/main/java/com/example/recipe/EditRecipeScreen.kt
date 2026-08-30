package com.example.recipe

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun EditRecipeScreen(
    navController: NavController,
    recipeId: Int
){

    val focusManager = LocalFocusManager.current

    //ViewModel
    val viewModel: EditRecipeViewModel = hiltViewModel()

    //戻る確認ダイアログ用
    var showBackDialog by remember {
        mutableStateOf(false)
    }

    //アプリ状態
    val context = LocalContext.current

    //画像選択用
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.updateImageUri(it.toString())
        }
    }

    // 撮影画像URI
    var cameraImageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }

    // カメラ起動
    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                viewModel.updateImageUri(
                    cameraImageUri.toString()
                )
            }
        }

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


        Text(
            text = "レシピ編集",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 6.dp)
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
                viewModel.updateRecipe()
                navController.popBackStack()
            },
            onBackClick = {
                navController.popBackStack()
            },
            hasChanges = viewModel.hasChanges(),
            onImageSelectClick = {
                imagePickerLauncher.launch("image/*")
            },
            onTakePhotoClick = {
                cameraImageUri = ImageUtils.createImageUri(context)
                cameraLauncher.launch(cameraImageUri)
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
