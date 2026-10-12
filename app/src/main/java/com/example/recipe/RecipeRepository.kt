package com.example.recipe

import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    //レシピ追加
    suspend fun insert(recipe: Recipe)

    //レシピ更新
    suspend fun update(recipe: Recipe)

    //IDを指定してレシピを1件取得
    suspend fun getRecipeById(id: Int): Recipe?

    //全レシピ取得
    fun getAll(): Flow<List<Recipe>>

    //レシピ削除
    suspend fun delete(recipe: Recipe)

    //レシピ検索（登録が古い順）
    fun searchRecipeByOldest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>>

    //レシピ検索（登録が新しい順）
    fun searchRecipeByNewest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>>

    //レシピ検索（名前順）
    fun searchRecipeByNameAsc(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>>

    //お気に入り状態更新
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )

    //レシピ一括削除
    suspend fun deleteRecipes(recipes: List<Recipe>)

    //レシピ一括更新
    suspend fun updateRecipes(recipes: List<Recipe>)

    //食事記録追加
    suspend fun insertMealRecord(mealRecord: MealRecord)

    //指定日の食事記録取得
    fun getMealRecordsByDate(
        date: String
    ): Flow<List<MealRecord>>

    //食事記録削除
    suspend fun deleteMealRecord(mealRecord: MealRecord)

    //指定日に食べたレシピ情報を取得
    fun getMealRecordsWithRecipeByDate(
        date: String
    ): Flow<List<MealRecordWithRecipe>>

    //指定月に食べたレシピ情報を取得
    fun getMealRecordsWithRecipeByMonth(
        yearMonth: String
    ): Flow<List<MealRecordWithRecipe>>

    //指定IDの食事記録を削除
    suspend fun deleteMealRecordById(id: Int)
}