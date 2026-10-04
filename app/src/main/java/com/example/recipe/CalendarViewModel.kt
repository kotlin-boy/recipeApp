package com.example.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import java.time.LocalDate
import kotlinx.coroutines.launch

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    // 選択中の日付
    private val selectedDate = MutableStateFlow(
        LocalDate.now().toString()
    )

    // 選択した日に食べたレシピ
    val mealRecords = selectedDate
        .flatMapLatest { date ->
            repository.getMealRecordsWithRecipeByDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 日付選択
    fun selectDate(date: String) {
        selectedDate.value = date
    }

    // 選択した日に食べたレシピを登録
    fun addMealRecord(recipeId: Int) {
        viewModelScope.launch {
            repository.insertMealRecord(
                MealRecord(
                    recipeId = recipeId,
                    date = selectedDate.value
                )
            )
        }
    }

    // 登録済みレシピ一覧
    val recipes = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}