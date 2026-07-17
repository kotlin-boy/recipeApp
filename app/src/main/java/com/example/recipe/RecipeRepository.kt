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
        genre: RecipeGenre?
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByOldest(keyword, genre)
    }

    //レシピID降順取得
    fun searchRecipeByNewest(
        keyword: String,
        genre: RecipeGenre?
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByNewest(keyword, genre)
    }

    //レシピ名昇順取得
    fun searchRecipeByNameAsc(
        keyword: String,
        genre: RecipeGenre?
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByNameAsc(keyword, genre)
    }
}