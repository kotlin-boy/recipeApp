package com.example.recipe

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) {
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)

    }

    suspend fun getRecipeById(id: Int): Recipe? {
        return recipeDao.getRecipeById(id)
    }

    fun getAll(): Flow<List<Recipe>> {
        return recipeDao.getAll()
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    //レシピID昇順取得
    fun searchRecipeByOldest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByOldest(keyword, genre, favoriteOnly)
    }

    //レシピID降順取得
    fun searchRecipeByNewest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByNewest(keyword, genre, favoriteOnly)
    }

    //レシピ名昇順取得
    fun searchRecipeByNameAsc(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByNameAsc(keyword, genre, favoriteOnly)
    }

    //お気に入り更新
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        recipeDao.updateFavorite(id, isFavorite)
    }

    //一括削除
    suspend fun deleteRecipes(recipes: List<Recipe>) {
        recipeDao.deleteRecipes(recipes)
    }

    //一括お気に入り更新
    suspend fun updateRecipes(recipes: List<Recipe>) {
        recipeDao.updateRecipes(recipes)
    }
}