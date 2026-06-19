package com.example.recipe

data class Recipe(
    val id: Int = 0,
    val name: String,
    val genre: RecipeGenre,
    val ingredients: List<String>,
    val steps: List<String>,
    val memo: String
)