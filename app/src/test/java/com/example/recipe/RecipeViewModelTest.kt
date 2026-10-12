package com.example.recipe

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeRecipeRepository
    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeRecipeRepository()
        viewModel = RecipeViewModel(fakeRepository)
    }

    //レシピを指定して選択モードに入れる
    @Test
    fun enterSelectionMode_selectsRecipe() {
        viewModel.enterSelectionMode(1)

        assertTrue(viewModel.isSelectionMode)
        assertTrue(viewModel.selectedIds.contains(1))
    }

    //選択モードを終了すると選択状態をすべて解除する
    @Test
    fun exitSelectionMode_clearsSelection() {
        viewModel.enterSelectionMode(1)
        viewModel.enterSelectionMode(2)

        viewModel.exitSelectionMode()

        assertFalse(viewModel.isSelectionMode)
        assertTrue(viewModel.selectedIds.isEmpty())
    }

    //選択中のレシピを再度選択すると選択を解除する
    @Test
    fun toggleSelection_selectedRecipe_removesSelection() {
        viewModel.enterSelectionMode(1)

        viewModel.toggleSelection(1)

        assertFalse(viewModel.isSelectionMode)
        assertTrue(viewModel.selectedIds.isEmpty())
    }

    //お気に入りではないレシピをお気に入りに変更する
    @Test
    fun toggleFavorite_notFavorite_updatesToFavorite() = runTest {
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

        viewModel.toggleFavorite(recipe)

        //viewModelScopeの処理が終わるまで実行
        advanceUntilIdle()

        //Repositoryに正しい値が渡されたことを確認
        assertEquals(1, fakeRepository.updatedFavoriteId)
        assertEquals(true, fakeRepository.updatedFavoriteValue)
    }

    //お気に入りフィルターを切り替えられる
    @Test
    fun toggleFavoriteFilter_changesFavoriteOnly() {
        assertFalse(viewModel.favoriteOnly)

        viewModel.toggleFavoriteFilter()

        assertTrue(viewModel.favoriteOnly)

        viewModel.toggleFavoriteFilter()

        assertFalse(viewModel.favoriteOnly)
    }

    //ソート順を変更できる
    @Test
    fun updateSortOrder_changesSortOrder() {
        assertEquals(SortOrder.OLDEST, viewModel.sortOrder)

        viewModel.updateSortOrder(SortOrder.NEWEST)

        assertEquals(SortOrder.NEWEST, viewModel.sortOrder)

        viewModel.updateSortOrder(SortOrder.NAME)

        assertEquals(SortOrder.NAME, viewModel.sortOrder)
    }

    //選択したレシピを一括でお気に入りに変更する
    @Test
    fun favoriteSelected_updatesSelectedRecipes() = runTest {
        val recipe1 = Recipe(
            id = 1,
            name = "カレー",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("玉ねぎ"),
            steps = listOf("煮込む"),
            memo = "",
            isFavorite = false,
            imageUri = ""
        )

        val recipe2 = Recipe(
            id = 2,
            name = "パスタ",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("パスタ"),
            steps = listOf("茹でる"),
            memo = "",
            isFavorite = false,
            imageUri = ""
        )

        //FakeRepositoryから返すレシピを設定
        fakeRepository.recipesToReturn = listOf(
            recipe1,
            recipe2
        )

        //設定したレシピを読み込ませる
        viewModel.updateSearchKeyword("")

        advanceUntilIdle()

        //ID=1のレシピだけ選択
        viewModel.enterSelectionMode(1)

        //一括お気に入り
        viewModel.favoriteSelected()

        advanceUntilIdle()

        //選択したレシピだけ更新されたことを確認
        assertEquals(
            1,
            fakeRepository.updatedRecipes.size
        )
        assertEquals(
            1,
            fakeRepository.updatedRecipes[0].id
        )
        assertTrue(
            fakeRepository.updatedRecipes[0].isFavorite
        )

        //一括処理後に選択モードが終了することを確認
        assertFalse(viewModel.isSelectionMode)
        assertTrue(viewModel.selectedIds.isEmpty())
    }

    //選択したレシピを一括でお気に入り解除する
    @Test
    fun unFavoriteSelected_updatesSelectedRecipes() = runTest {
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
            name = "パスタ",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("パスタ"),
            steps = listOf("茹でる"),
            memo = "",
            isFavorite = true,
            imageUri = ""
        )

        //FakeRepositoryから返すレシピを設定
        fakeRepository.recipesToReturn = listOf(
            recipe1,
            recipe2
        )

        //設定したレシピを読み込ませる
        viewModel.updateSearchKeyword("")

        advanceUntilIdle()

        //ID=1のレシピだけ選択
        viewModel.enterSelectionMode(1)

        //一括お気に入り解除
        viewModel.unFavoriteSelected()

        advanceUntilIdle()

        //選択したレシピだけ更新されたことを確認
        assertEquals(
            1,
            fakeRepository.updatedRecipes.size
        )
        assertEquals(
            1,
            fakeRepository.updatedRecipes[0].id
        )
        assertFalse(
            fakeRepository.updatedRecipes[0].isFavorite
        )

        //一括処理後に選択モードが終了することを確認
        assertFalse(viewModel.isSelectionMode)
        assertTrue(viewModel.selectedIds.isEmpty())
    }

    //選択したレシピだけを一括削除する
    @Test
    fun deleteSelected_deletesSelectedRecipes() = runTest {
        val recipe1 = Recipe(
            id = 1,
            name = "カレー",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("玉ねぎ"),
            steps = listOf("煮込む"),
            memo = "",
            isFavorite = false,
            imageUri = ""
        )

        val recipe2 = Recipe(
            id = 2,
            name = "パスタ",
            genre = RecipeGenre.MAIN,
            ingredients = listOf("パスタ"),
            steps = listOf("茹でる"),
            memo = "",
            isFavorite = false,
            imageUri = ""
        )

        //FakeRepositoryから返すレシピを設定
        fakeRepository.recipesToReturn = listOf(
            recipe1,
            recipe2
        )

        //設定したレシピを読み込ませる
        viewModel.updateSearchKeyword("")

        advanceUntilIdle()

        //ID=2のレシピだけ選択
        viewModel.enterSelectionMode(2)

        //一括削除
        viewModel.deleteSelected()

        advanceUntilIdle()

        //選択したレシピだけ削除対象になったことを確認
        assertEquals(
            1,
            fakeRepository.deletedRecipes.size
        )
        assertEquals(
            2,
            fakeRepository.deletedRecipes[0].id
        )

        //一括処理後に選択モードが終了することを確認
        assertFalse(viewModel.isSelectionMode)
        assertTrue(viewModel.selectedIds.isEmpty())
    }

    //検索キーワードを変更できる
    @Test
    fun updateSearchKeyword_changesSearchKeyword() {
        assertEquals("", viewModel.searchKeyword)

        viewModel.updateSearchKeyword("カレー")

        assertEquals(
            "カレー",
            viewModel.searchKeyword
        )
    }

    //ジャンルフィルターを変更できる
    @Test
    fun updateSelectedGenre_changesSelectedGenre() {
        assertEquals(null, viewModel.selectedGenre)

        viewModel.updateSelectedGenre(RecipeGenre.MAIN)

        assertEquals(
            RecipeGenre.MAIN,
            viewModel.selectedGenre
        )

        //フィルターを解除できることも確認
        viewModel.updateSelectedGenre(null)

        assertEquals(
            null,
            viewModel.selectedGenre
        )
    }
}