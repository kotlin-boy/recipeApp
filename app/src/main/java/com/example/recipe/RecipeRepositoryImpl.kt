package com.example.recipe

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val mealRecordDao: MealRecordDao
) : RecipeRepository {

    //レシピ追加
    override suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    //レシピ更新
    override suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    //IDを指定してレシピを1件取得
    override suspend fun getRecipeById(id: Int): Recipe? {
        return recipeDao.getRecipeById(id)
    }

    //全レシピ取得
    override fun getAll(): Flow<List<Recipe>> {
        return recipeDao.getAll()
    }

    //レシピ削除
    override suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    //レシピ検索（登録が古い順）
    override fun searchRecipeByOldest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByOldest(
            keyword,
            genre,
            favoriteOnly
        )
    }

    //レシピ検索（登録が新しい順）
    override fun searchRecipeByNewest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByNewest(
            keyword,
            genre,
            favoriteOnly
        )
    }

    //レシピ検索（名前順）
    override fun searchRecipeByNameAsc(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return recipeDao.searchRecipeByNameAsc(
            keyword,
            genre,
            favoriteOnly
        )
    }

    //お気に入り状態更新
    override suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        recipeDao.updateFavorite(id, isFavorite)
    }

    //レシピ一括削除
    override suspend fun deleteRecipes(recipes: List<Recipe>) {
        recipeDao.deleteRecipes(recipes)
    }

    //レシピ一括更新
    override suspend fun updateRecipes(recipes: List<Recipe>) {
        recipeDao.updateRecipes(recipes)
    }

    //食事記録追加
    override suspend fun insertMealRecord(mealRecord: MealRecord) {
        mealRecordDao.insert(mealRecord)
    }

    //指定日の食事記録取得
    override fun getMealRecordsByDate(
        date: String
    ): Flow<List<MealRecord>> {
        return mealRecordDao.getMealRecordsByDate(date)
    }

    //食事記録削除
    override suspend fun deleteMealRecord(mealRecord: MealRecord) {
        mealRecordDao.delete(mealRecord)
    }

    //指定日に食べたレシピ情報を取得
    override fun getMealRecordsWithRecipeByDate(
        date: String
    ): Flow<List<MealRecordWithRecipe>> {
        return mealRecordDao.getMealRecordsWithRecipeByDate(date)
    }

    //指定月に食べたレシピ情報を取得
    override fun getMealRecordsWithRecipeByMonth(
        yearMonth: String
    ): Flow<List<MealRecordWithRecipe>> {
        return mealRecordDao.getMealRecordsWithRecipeByMonth(yearMonth)
    }

    //指定IDの食事記録を削除
    override suspend fun deleteMealRecordById(id: Int) {
        mealRecordDao.deleteMealRecordById(id)
    }
}