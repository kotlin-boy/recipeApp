package com.example.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    //レシピ格納
    private val _recipes =
        MutableStateFlow<List<Recipe>>(emptyList())

    //読み取り変数へ
    val recipes = _recipes.asStateFlow()

    //検索用
    var searchKeyword by mutableStateOf("")
        private set

    //ジャンルフィルター用
    var selectedGenre by mutableStateOf<RecipeGenre?>(null)
        private set

    //ソート用
    var sortOrder by mutableStateOf(SortOrder.OLDEST)
        private set

    //お気に入りフィルター
    var favoriteOnly by mutableStateOf(false)
        private set

    //ソート種類判別用
    fun updateSortOrder(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        loadRecipes()
    }

    //初期画面のレシピ
    init {
        loadRecipes()
    }

    //レシピ読込
    private fun loadRecipes() {
        viewModelScope.launch {
            val recipeFlow = when (sortOrder) {
                SortOrder.OLDEST -> {
                    repository.searchRecipeByOldest(
                        keyword = searchKeyword,
                        genre = selectedGenre,
                        favoriteOnly = favoriteOnly
                    )
                }
                SortOrder.NEWEST -> {
                    repository.searchRecipeByNewest(
                        keyword = searchKeyword,
                        genre = selectedGenre,
                        favoriteOnly = favoriteOnly
                    )
                }
                SortOrder.NAME -> {
                    repository.searchRecipeByNameAsc(
                        keyword = searchKeyword,
                        genre = selectedGenre,
                        favoriteOnly = favoriteOnly
                    )
                }
            }
            recipeFlow.collectLatest { recipeList ->
                _recipes.value = recipeList
            }
        }
    }

    //検索関数
    fun updateSearchKeyword(keyword: String) {
        searchKeyword = keyword
        loadRecipes()
    }

    //ジャンルフィルター
    fun updateSelectedGenre(
        genre: RecipeGenre?
    ) {
        selectedGenre = genre
        loadRecipes()
    }

    //お気に入りトグル
    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.updateFavorite(
                recipe.id,
                !recipe.isFavorite
            )

        }
    }

    //お気に入りフィルター
    fun toggleFavoriteFilter() {
        favoriteOnly = !favoriteOnly
        loadRecipes()
    }
}

