package com.example.recipe

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest


@OptIn(ExperimentalCoroutinesApi::class)
class AddRecipeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeRecipeRepository
    private lateinit var viewModel: AddRecipeViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeRecipeRepository()
        viewModel = AddRecipeViewModel(fakeRepository)
    }

    //初期状態
    @Test
    fun initialState_hasNoChanges() {
        assertFalse(viewModel.hasChanges())
    }

    //お気に入りを切り替え
    @Test
    fun toggleFavorite_changesFavoriteState() {
        assertFalse(viewModel.isFavorite)

        viewModel.toggleFavorite()

        assertTrue(viewModel.isFavorite)
    }

    //材料が1つだけの場合の材料削除
    @Test
    fun removeIngredient_whenOnlyOne_remainsEmptyItem() {
        viewModel.updateIngredient(0, "玉ねぎ")

        viewModel.removeIngredient(0)

        assertEquals(1, viewModel.ingredients.size)
        assertEquals("", viewModel.ingredients[0])
    }

    //レシピ名が空の場合は保存に失敗する
    @Test
    fun saveRecipe_emptyRecipeName_returnsFalse() {
        val result = viewModel.saveRecipe()

        assertFalse(result)
        assertEquals(
            "レシピ名を入力してください",
            viewModel.errorMessage
        )
    }

    //材料が空の場合は保存に失敗する
    @Test
    fun saveRecipe_emptyIngredients_returnsFalse() {
        viewModel.updateRecipeName("カレー")

        val result = viewModel.saveRecipe()

        assertFalse(result)
        assertEquals(
            "材料を入力してください",
            viewModel.errorMessage
        )
    }

    //手順が空の場合は保存に失敗する
    @Test
    fun saveRecipe_emptySteps_returnsFalse() {
        viewModel.updateRecipeName("カレー")
        viewModel.updateIngredient(0, "玉ねぎ")

        val result = viewModel.saveRecipe()

        assertFalse(result)
        assertEquals(
            "手順を入力してください",
            viewModel.errorMessage
        )
    }

    //正常な入力の場合はレシピを保存できる
    @Test
    fun saveRecipe_validInput_insertsRecipe() = runTest {
        //入力値を設定
        viewModel.updateRecipeName("カレー")
        viewModel.updateIngredient(0, "玉ねぎ")
        viewModel.addIngredient()
        viewModel.updateIngredient(1, "じゃがいも")

        viewModel.updateStep(0, "材料を切る")
        viewModel.addStep()
        viewModel.updateStep(1, "煮込む")

        viewModel.updateMemo("翌日も食べる")
        viewModel.updateImageUri("content://test-image")
        viewModel.toggleFavorite()

        //保存
        val result = viewModel.saveRecipe()

        //viewModelScopeの処理が終わるまで実行
        advanceUntilIdle()

        //保存が成功したことを確認
        assertTrue(result)

        //Repositoryに渡されたRecipeを取得
        val savedRecipe = fakeRepository.insertedRecipe

        //正しい内容で保存されたことを確認
        assertEquals("カレー", savedRecipe?.name)
        assertEquals(RecipeGenre.MAIN, savedRecipe?.genre)
        assertEquals(
            listOf("玉ねぎ", "じゃがいも"),
            savedRecipe?.ingredients
        )
        assertEquals(
            listOf("材料を切る", "煮込む"),
            savedRecipe?.steps
        )
        assertEquals("翌日も食べる", savedRecipe?.memo)
        assertEquals("content://test-image", savedRecipe?.imageUri)
        assertTrue(savedRecipe?.isFavorite == true)
    }

    //空の材料と手順は保存時に除外される
    @Test
    fun saveRecipe_blankItems_areExcluded() = runTest {
        //入力値を設定
        viewModel.updateRecipeName("カレー")

        viewModel.updateIngredient(0, "玉ねぎ")
        viewModel.addIngredient()
        viewModel.updateIngredient(1, "")

        viewModel.updateStep(0, "材料を切る")
        viewModel.addStep()
        viewModel.updateStep(1, "")

        //保存
        val result = viewModel.saveRecipe()

        //viewModelScopeの処理が終わるまで実行
        advanceUntilIdle()

        //保存が成功したことを確認
        assertTrue(result)

        val savedRecipe = fakeRepository.insertedRecipe

        //空の要素が除外されていることを確認
        assertEquals(
            listOf("玉ねぎ"),
            savedRecipe?.ingredients
        )
        assertEquals(
            listOf("材料を切る"),
            savedRecipe?.steps
        )
    }
}