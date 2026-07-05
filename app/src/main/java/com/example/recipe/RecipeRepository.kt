package com.example.recipe

import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) {
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }
}