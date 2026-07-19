package com.example.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val genre: RecipeGenre,
    val ingredients: List<String>,
    val steps: List<String>,
    val memo: String,
    val isFavorite: Boolean = false
)