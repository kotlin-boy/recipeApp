package com.example.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
//変数ゾーン

    //受け取りデータ
    var originalRecipe by mutableStateOf<Recipe?>(null)

    //レシピ名
    var recipeName by mutableStateOf("")

    //ジャンル名
    var selectedGenre by mutableStateOf(RecipeGenre.MAIN)

    //プルダウンメニューのON・OFF
    var expanded by mutableStateOf(false)

    //材料のリスト
    val ingredients = mutableStateListOf("")

    //手順のリスト
    val steps = mutableStateListOf("")

    //メモゾーン
    var memo by mutableStateOf("")

    //画像URI
    var imageUri by mutableStateOf("")
        private set

    //お気に入り状態
    var isFavorite by mutableStateOf(false)
        private set

    //関数ゾーン
    //レシピ名
    fun updateRecipeName(name: String) {
        recipeName = name
    }

    //ドロップダウン状態ON
    fun expendGenre() {
        expanded = true
    }

    //ドロップダウン状態OFF
    fun dismissGenre() {
        expanded = false
    }

    //ジャンル選択
    fun updateGenre(genre: RecipeGenre?) {
        genre ?: return
        selectedGenre = genre
        expanded = false
    }

    //材料更新
    fun updateIngredient(
        index: Int,
        value: String
    ) {
        ingredients[index] = value
    }

    //材料削除
    fun removeIngredient(index: Int) {
        if (ingredients.size == 1) {
            ingredients[index] = ""
        } else {
            ingredients.removeAt(index)
        }
    }

    //レシピ追加
    fun addIngredient() {
        ingredients.add("")
    }

    //手順更新
    fun updateStep(
        index: Int,
        value: String
    ) {
        steps[index] = value
    }

    //手順削除
    fun removeStep(index: Int) {
        if (steps.size == 1) {
            steps[index] = ""
        } else {
            steps.removeAt(index)
        }
    }

    //手順追加
    fun addStep() {
        steps.add("")
    }

    //メモ更新
    fun updateMemo(value: String) {
        memo = value
    }

    //画像更新
    fun updateImageUri(uri: String) {
        imageUri = uri
    }

    //画像削除
    fun removeImage() {
        imageUri = ""
    }


    //値を格納
    fun updateRecipe() {
        val recipe = Recipe(
            id = originalRecipe?.id ?: return,
            name = recipeName,
            genre = selectedGenre,
            ingredients = ingredients.toList(),
            steps = steps.toList(),
            memo = memo,
            isFavorite = isFavorite,
            imageUri = imageUri,
        )
        //DBに挿入　非同期処理
        viewModelScope.launch {
            repository.update(recipe)
        }
    }

    //IDからレシピを格納
    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            val recipe = repository.getRecipeById(id) ?: return@launch

            originalRecipe = recipe

            recipeName = recipe.name
            selectedGenre = recipe.genre

            ingredients.clear()
            ingredients.addAll(recipe.ingredients)

            steps.clear()
            steps.addAll(recipe.steps)

            memo = recipe.memo
            isFavorite = recipe.isFavorite
            imageUri = recipe.imageUri
        }
    }

    //お気に入りトグル
    fun toggleFavorite() {
        isFavorite = !isFavorite
    }

    //変更チェック
    fun hasChanges(): Boolean {
        val original = originalRecipe ?: return false

        return recipeName != original.name ||
                selectedGenre != original.genre ||
                ingredients.toList() != original.ingredients ||
                steps.toList() != original.steps ||
                memo != original.memo ||
                imageUri != original.imageUri ||
                isFavorite != original.isFavorite
    }
}