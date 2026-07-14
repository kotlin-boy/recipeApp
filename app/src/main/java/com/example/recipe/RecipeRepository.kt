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

    fun searchRecipe(
        keyword: String,
        genre: RecipeGenre?
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipe(keyword, genre)
    }
}