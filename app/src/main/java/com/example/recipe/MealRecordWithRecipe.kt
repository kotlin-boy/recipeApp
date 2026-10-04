package com.example.recipe
data class MealRecordWithRecipe(
    val mealRecordId: Int,
    val recipeId: Int,
    val date: String,
    val recipeName: String
)