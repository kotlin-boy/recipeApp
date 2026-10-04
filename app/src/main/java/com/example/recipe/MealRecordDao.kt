package com.example.recipe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealRecordDao {

    // 食べたレシピを記録
    @Insert
    suspend fun insert(mealRecord: MealRecord)

    // 指定した日に食べた記録を取得
    @Query(
        """
        SELECT * FROM meal_records
        WHERE date = :date
        """
    )
    fun getMealRecordsByDate(date: String): Flow<List<MealRecord>>

    // 食事記録を削除
    @Delete
    suspend fun delete(mealRecord: MealRecord)

    @Query(
        """
    SELECT
        meal_records.id AS mealRecordId,
        meal_records.recipeId AS recipeId,
        meal_records.date AS date,
        Recipe.name AS recipeName
    FROM meal_records
    INNER JOIN Recipe
        ON meal_records.recipeId = Recipe.id
    WHERE meal_records.date = :date
    """
    )
    fun getMealRecordsWithRecipeByDate(
        date: String
    ): Flow<List<MealRecordWithRecipe>>
}