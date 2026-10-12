package com.example.recipe

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.first

@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {

    private lateinit var database: RecipeDatabase
    private lateinit var recipeDao: RecipeDao

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
    }

    @After
    fun tearDown() {
        //テスト終了後にDBを閉じる
        database.close()
    }

    //レシピを登録してIDから取得できる
    @Test
    fun insertAndGetRecipeById_returnsRecipe() = runBlocking {
        val recipe = Recipe(
            id = 1,
            name = "カレー",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("玉ねぎ"),
            steps = listOf("煮込む"),
            memo = "テスト",
            isFavorite = false,
            imageUri = ""
        )

        //レシピを登録
        recipeDao.insert(recipe)

        //登録したレシピを取得
        val result = recipeDao.getRecipeById(1)

        //正しく登録されていることを確認
        assertNotNull(result)
        assertEquals("カレー", result?.name)
        assertEquals(RecipeGenre.MAIN, result?.genre)
        assertEquals(
            listOf("玉ねぎ"),
            result?.ingredients
        )
        assertEquals(
            listOf("煮込む"),
            result?.steps
        )
        assertEquals("テスト", result?.memo)
        assertEquals(false, result?.isFavorite)
    }

    //検索キーワードとお気に入り条件でレシピを取得できる
    @Test
    fun searchRecipeByOldest_filtersKeywordAndFavorite() = runBlocking {
        val recipe1 = Recipe(
            id = 1,
            name = "カレー",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("玉ねぎ"),
            steps = listOf("煮込む"),
            memo = "",
            isFavorite = true,
            imageUri = ""
        )

        val recipe2 = Recipe(
            id = 2,
            name = "カレーうどん",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("うどん"),
            steps = listOf("煮込む"),
            memo = "",
            isFavorite = false,
            imageUri = ""
        )

        val recipe3 = Recipe(
            id = 3,
            name = "パスタ",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("パスタ"),
            steps = listOf("茹でる"),
            memo = "",
            isFavorite = true,
            imageUri = ""
        )

        //3件のレシピを登録
        recipeDao.insert(recipe1)
        recipeDao.insert(recipe2)
        recipeDao.insert(recipe3)

        //「カレー」を含み、お気に入りのレシピだけ取得
        val result = recipeDao.searchRecipeByOldest(
            keyword = "カレー",
            genre = null,
            favoriteOnly = true
        ).first()

        //カレーだけが取得されることを確認
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("カレー", result[0].name)
        assertEquals(true, result[0].isFavorite)
    }
}