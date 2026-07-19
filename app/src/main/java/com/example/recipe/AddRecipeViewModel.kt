package com.example.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
//変数ゾーン
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

    //エラーメッセージ用
    var errorMessage by mutableStateOf<String?>(null)

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
        if (ingredients.size > 1) {
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
        if (steps.size > 1)
            steps.removeAt(index)
    }

    //手順追加
    fun addStep() {
        steps.add("")
    }

    //メモ更新
    fun updateMemo(value: String) {
        memo = value
    }

    //値を格納
    fun saveRecipe() : Boolean{
        //空要素チェック
        if (recipeName.isBlank()) {
            errorMessage = "レシピ名を入力してください"
            return false
        }
        if (
            ingredients.all {
                it.isBlank()
            }
        ) {
            errorMessage = "材料を入力してください"
            return false
        }
        if (
            steps.all {
                it.isBlank()
            }
        ) {
            errorMessage = "手順を入力してください"
            return false
        }

        errorMessage = null

        //保存フェーズ
        val recipe = Recipe(
            id = 0,
            name = recipeName,
            genre = selectedGenre,
            ingredients = ingredients.filter {
                it.isNotBlank()
            },
            steps = steps.filter {
                it.isNotBlank()
            },
            memo = memo
        )
        //DBに挿入　非同期処理
        viewModelScope.launch {
            repository.insert(recipe)
        }

        return true
    }

    //エラーメッセージ初期化
    fun clearErrorMessage() {
        errorMessage = null
    }

    //お気に入りトグル
    fun toggleFavorite() {
        isFavorite = !isFavorite
    }
}
