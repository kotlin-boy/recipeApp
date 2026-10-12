package com.example.recipe

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeRecipeRepository
    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeRecipeRepository()
        viewModel = CalendarViewModel(fakeRepository)
    }

    //選択中の日付を変更できる
    @Test
    fun selectDate_changesSelectedDate() {
        viewModel.selectDate("2026-10-15")

        assertEquals(
            "2026-10-15",
            viewModel.selectedDate.value
        )
    }

    //現在表示している月から1か月前に移動できる
    @Test
    fun previousMonth_movesToPreviousMonth() {
        val before = viewModel.currentMonth.value

        viewModel.previousMonth()

        assertEquals(
            before.minusMonths(1),
            viewModel.currentMonth.value
        )
    }

    //現在表示している月から1か月後に移動できる
    @Test
    fun nextMonth_movesToNextMonth() {
        val before = viewModel.currentMonth.value

        viewModel.nextMonth()

        assertEquals(
            before.plusMonths(1),
            viewModel.currentMonth.value
        )
    }

    //選択した日付に食事記録を追加できる
    @Test
    fun addMealRecord_insertsSelectedDateAndRecipeId() = runTest {
        //日付を選択
        viewModel.selectDate("2026-10-15")

        //ID=3のレシピを食事記録に追加
        viewModel.addMealRecord(3)

        //viewModelScopeの処理が終わるまで実行
        advanceUntilIdle()

        //Repositoryに渡された食事記録を取得
        val insertedMealRecord = fakeRepository.insertedMealRecord

        //正しいレシピIDと日付で登録されたことを確認
        assertEquals(
            3,
            insertedMealRecord?.recipeId
        )
        assertEquals(
            "2026-10-15",
            insertedMealRecord?.date
        )
    }

    //選択した日の食事記録を取得できる
    @Test
    fun mealRecords_selectedDate_returnsMealRecords() = runTest {
        val mealRecord = MealRecordWithRecipe(
            mealRecordId = 1,
            recipeId = 3,
            date = "2026-10-15",
            recipeName = "カレー"
        )

        //FakeRepositoryから返す食事記録を設定
        fakeRepository.mealRecordsByDateToReturn = listOf(
            mealRecord
        )

        //日付を選択
        viewModel.selectDate("2026-10-15")

        //mealRecordsを監視してFlowを動かす
        backgroundScope.launch(
            UnconfinedTestDispatcher(testScheduler)
        ) {
            viewModel.mealRecords.collect {
                //Flowの監視のみ
            }
        }

        advanceUntilIdle()

        //取得した食事記録を確認
        assertEquals(
            1,
            viewModel.mealRecords.value.size
        )
        assertEquals(
            3,
            viewModel.mealRecords.value[0].recipeId
        )
        assertEquals(
            "カレー",
            viewModel.mealRecords.value[0].recipeName
        )
    }

    //表示中の月の食事記録を取得できる
    @Test
    fun monthMealRecords_currentMonth_returnsMealRecords() = runTest {
        val mealRecord = MealRecordWithRecipe(
            mealRecordId = 1,
            recipeId = 3,
            date = "2026-10-15",
            recipeName = "カレー"
        )

        //FakeRepositoryから返す月の食事記録を設定
        fakeRepository.mealRecordsByMonthToReturn = listOf(
            mealRecord
        )

        //monthMealRecordsを監視してFlowを動かす
        backgroundScope.launch(
            UnconfinedTestDispatcher(testScheduler)
        ) {
            viewModel.monthMealRecords.collect {
                //Flowの監視のみ
            }
        }

        advanceUntilIdle()

        //取得した食事記録を確認
        assertEquals(
            1,
            viewModel.monthMealRecords.value.size
        )
        assertEquals(
            3,
            viewModel.monthMealRecords.value[0].recipeId
        )
        assertEquals(
            "カレー",
            viewModel.monthMealRecords.value[0].recipeName
        )
    }

    //指定した食事記録を削除できる
    @Test
    fun deleteMealRecord_deletesSelectedMealRecord() = runTest {
        //ID=5の食事記録を削除
        viewModel.deleteMealRecord(5)

        //viewModelScopeの処理が終わるまで実行
        advanceUntilIdle()

        //正しい食事記録IDがRepositoryに渡されたことを確認
        assertEquals(
            5,
            fakeRepository.deletedMealRecordId
        )
    }
}