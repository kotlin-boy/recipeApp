package com.example.recipe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    //レシピ登録
    @Insert
    suspend fun insert(
        recipe: Recipe
    )

    //レシピ全件取得
    @Query("SELECT * FROM Recipe")
    fun getAll(): Flow<List<Recipe>>

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

    @Query("""
        SELECT *
        FROM Recipe
        WHERE name LIKE '%' || :keyword || '%' 
        AND (:genre IS NULL OR genre = :genre)
    """)
    fun searchRecipe(
        keyword: String,
        genre: RecipeGenre?
    ): Flow<List<Recipe>>
}