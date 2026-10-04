package com.example.recipe

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import java.time.LocalDate
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.collectLatest
import java.time.YearMonth

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    //選択中の日付
    private val _selectedDate = MutableStateFlow(
        LocalDate.now().toString()
    )
    //読み取り専用
    val selectedDate = _selectedDate.asStateFlow()

    //選択した日に食べたレシピ
    val mealRecords = _selectedDate
        .flatMapLatest { date ->
            repository.getMealRecordsWithRecipeByDate(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //日付選択
    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    //選択した日に食べたレシピを登録
    fun addMealRecord(recipeId: Int) {
        viewModelScope.launch {
            repository.insertMealRecord(
                MealRecord(
                    recipeId = recipeId,
                    date = _selectedDate.value
                )
            )
        }
    }

    //登録済みレシピ一覧
    private val _recipes =
        MutableStateFlow<List<Recipe>>(emptyList())

    val recipes = _recipes.asStateFlow()

    //現在年月
    private val _currentMonth = MutableStateFlow(
        YearMonth.now()
    )
    val currentMonth = _currentMonth.asStateFlow()

    //名前検索
    var searchKeyword by mutableStateOf("")
        private set

    //ジャンルフィルター
    var selectedGenre by mutableStateOf<RecipeGenre?>(null)
        private set

    //ソート
    var sortOrder by mutableStateOf(SortOrder.OLDEST)
        private set

    //お気に入りフィルター
    var favoriteOnly by mutableStateOf(false)
        private set

    //画像表示
    var showImage by mutableStateOf(false)
        private set

    fun toggleShowImage() {
        showImage = !showImage
    }

   //名前検索
    fun updateSearchKeyword(keyword: String) {
        searchKeyword = keyword
        loadRecipes()
    }

    //ジャンル検索
    fun updateSelectedGenre(genre: RecipeGenre?) {
        selectedGenre = genre
        loadRecipes()
    }

    //ソート
    fun updateSortOrder(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        loadRecipes()
    }

    //お気に入りフィルター
    fun toggleFavoriteFilter() {
        favoriteOnly = !favoriteOnly
        loadRecipes()
    }

    //前月表示
    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    //翌月表示
    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    //月の食事記録取得
    val monthMealRecords = _currentMonth
        .flatMapLatest { yearMonth ->
            repository.getMealRecordsWithRecipeByMonth(
                yearMonth.toString()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadRecipes()
    }

    //レシピ読み込み
    private fun loadRecipes() {
        viewModelScope.launch {

            val recipeFlow = when (sortOrder) {
                SortOrder.OLDEST -> {
                    repository.searchRecipeByOldest(
                        keyword = searchKeyword,
                        genre = selectedGenre,
                        favoriteOnly = favoriteOnly
                    )
                }

                SortOrder.NEWEST -> {
                    repository.searchRecipeByNewest(
                        keyword = searchKeyword,
                        genre = selectedGenre,
                        favoriteOnly = favoriteOnly
                    )
                }

                SortOrder.NAME -> {
                    repository.searchRecipeByNameAsc(
                        keyword = searchKeyword,
                        genre = selectedGenre,
                        favoriteOnly = favoriteOnly
                    )
                }
            }

            recipeFlow.collectLatest { recipeList ->
                _recipes.value = recipeList
            }
        }
    }

}
