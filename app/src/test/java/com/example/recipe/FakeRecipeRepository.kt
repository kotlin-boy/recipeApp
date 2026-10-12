package com.example.recipe

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

//Unit Test用のRepository
class FakeRecipeRepository : RecipeRepository {

    //insertされたRecipeを保持
    var insertedRecipe: Recipe? = null
        private set

    //テストで検索結果として返すレシピ一覧
    var recipesToReturn: List<Recipe> = emptyList()

    //お気に入り更新時のレシピIDを保持
    var updatedFavoriteId: Int? = null
        private set

    //お気に入り更新時の状態を保持
    var updatedFavoriteValue: Boolean? = null
        private set

    //一括更新されたレシピを保持
    var updatedRecipes: List<Recipe> = emptyList()
        private set

    //一括削除されたレシピを保持
    var deletedRecipes: List<Recipe> = emptyList()
        private set

    //追加された食事記録を保持
    var insertedMealRecord: MealRecord? = null
        private set

    //テストで日付検索結果として返す食事記録
    var mealRecordsByDateToReturn: List<MealRecordWithRecipe> = emptyList()

    //テストで月検索結果として返す食事記録
    var mealRecordsByMonthToReturn: List<MealRecordWithRecipe> = emptyList()

    //削除された食事記録IDを保持
    var deletedMealRecordId: Int? = null
        private set

    //追加されたレシピを保持
    override suspend fun insert(recipe: Recipe) {
        insertedRecipe = recipe
    }

    //今回は使用しない
    override suspend fun update(recipe: Recipe) {
    }

    //今回は使用しないためnullを返す
    override suspend fun getRecipeById(id: Int): Recipe? {
        return null
    }

    //今回は使用しないため空のリストを返す
    override fun getAll(): Flow<List<Recipe>> {
        return flowOf(emptyList())
    }

    //今回は使用しない
    override suspend fun delete(recipe: Recipe) {
    }

    //登録が古い順の検索結果を返す
    override fun searchRecipeByOldest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return flowOf(recipesToReturn)
    }

    //登録が新しい順の検索結果を返す
    override fun searchRecipeByNewest(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return flowOf(recipesToReturn)
    }

    //名前順の検索結果を返す
    override fun searchRecipeByNameAsc(
        keyword: String,
        genre: RecipeGenre?,
        favoriteOnly: Boolean
    ): Flow<List<Recipe>> {
        return flowOf(recipesToReturn)
    }

    //お気に入り更新時の値を保持
    override suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        updatedFavoriteId = id
        updatedFavoriteValue = isFavorite
    }

    //一括削除されたレシピを保持
    override suspend fun deleteRecipes(
        recipes: List<Recipe>
    ) {
        deletedRecipes = recipes
    }

    //一括更新されたレシピを保持
    override suspend fun updateRecipes(
        recipes: List<Recipe>
    ) {
        updatedRecipes = recipes
    }

    //追加された食事記録を保持
    override suspend fun insertMealRecord(
        mealRecord: MealRecord
    ) {
        insertedMealRecord = mealRecord
    }

    //今回は使用しないため空のリストを返す
    override fun getMealRecordsByDate(
        date: String
    ): Flow<List<MealRecord>> {
        return flowOf(emptyList())
    }

    //今回は使用しない
    override suspend fun deleteMealRecord(
        mealRecord: MealRecord
    ) {
    }

    //指定日の食事記録を返す
    override fun getMealRecordsWithRecipeByDate(
        date: String
    ): Flow<List<MealRecordWithRecipe>> {
        return flowOf(mealRecordsByDateToReturn)
    }

    //指定月の食事記録を返す
    override fun getMealRecordsWithRecipeByMonth(
        yearMonth: String
    ): Flow<List<MealRecordWithRecipe>> {
        return flowOf(mealRecordsByMonthToReturn)
    }

    //削除された食事記録IDを保持
    override suspend fun deleteMealRecordById(id: Int) {
        deletedMealRecordId = id
    }
}