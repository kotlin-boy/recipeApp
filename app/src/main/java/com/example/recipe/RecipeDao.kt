package com.example.recipe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {

    //レシピ登録
    @Insert
    suspend fun insert(
        recipe: Recipe
    )

    //レシピ全件取得
    @Query("SELECT * FROM Recipe")
    suspend fun getAll(): List<Recipe>

    //指定IDレシピ取得
    @Query(
        "SELECT * FROM Recipe WHERE id = :recipeId"
    )
    suspend fun getRecipeById(
        recipeId: Int
    ): Recipe?

    @Delete
    suspend fun delete(
        recipe: Recipe
    )

    @Update
    suspend fun update(
        recipe: Recipe
    )
}