package com.example.recipe

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealRecordDaoTest {

    private lateinit var database: RecipeDatabase
    private lateinit var recipeDao: RecipeDao
    private lateinit var mealRecordDao: MealRecordDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        //テスト用のDBをメモリ上に作成
        database = Room.inMemoryDatabaseBuilder(
            context,
            RecipeDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        recipeDao = database.recipeDao()
        mealRecordDao = database.mealRecordDao()
    }

    @After
    fun tearDown() {
        //テスト終了後にDBを閉じる
        database.close()
    }

    //指定日に食べたレシピをRecipeと結合して取得できる
    @Test
    fun getMealRecordsWithRecipeByDate_returnsJoinedRecipe() = runBlocking {
        val recipe = Recipe(
            id = 1,
            name = "カレー",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("玉ねぎ"),
            steps = listOf("煮込む"),
            memo = "",
            isFavorite = false,
            imageUri = ""
        )

        //レシピを登録
        recipeDao.insert(recipe)

        val mealRecord = MealRecord(
            recipeId = 1,
            date = "2026-10-15"
        )

        //食事記録を登録
        mealRecordDao.insert(mealRecord)

        //指定日の食事記録をRecipeと結合して取得
        val result = mealRecordDao
            .getMealRecordsWithRecipeByDate("2026-10-15")
            .first()

        //正しい食事記録とレシピ名を取得できたことを確認
        assertEquals(1, result.size)
        assertEquals(1, result[0].recipeId)
        assertEquals("2026-10-15", result[0].date)
        assertEquals("カレー", result[0].recipeName)
    }

    //指定月の食事記録を日付順で取得できる
    @Test
    fun getMealRecordsWithRecipeByMonth_returnsRecordsInDateOrder() = runBlocking {
        val recipe = Recipe(
            id = 1,
            name = "カレー",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("玉ねぎ"),
            steps = listOf("煮込む"),
            memo = "",
            isFavorite = false,
            imageUri = ""
        )

        //レシピを登録
        recipeDao.insert(recipe)

        //同じ月に2件、別の月に1件登録
        mealRecordDao.insert(
            MealRecord(
                recipeId = 1,
                date = "2026-10-20"
            )
        )
        mealRecordDao.insert(
            MealRecord(
                recipeId = 1,
                date = "2026-10-05"
            )
        )
        mealRecordDao.insert(
            MealRecord(
                recipeId = 1,
                date = "2026-11-01"
            )
        )

        //2026年10月の食事記録を取得
        val result = mealRecordDao
            .getMealRecordsWithRecipeByMonth("2026-10")
            .first()

        //10月の2件だけ取得できることを確認
        assertEquals(2, result.size)

        //日付の昇順で取得できることを確認
        assertEquals("2026-10-05", result[0].date)
        assertEquals("2026-10-20", result[1].date)

        //Recipeとの結合結果も確認
        assertEquals("カレー", result[0].recipeName)
        assertEquals("カレー", result[1].recipeName)
    }
}