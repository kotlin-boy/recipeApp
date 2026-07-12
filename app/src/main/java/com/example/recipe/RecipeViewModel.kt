package com.example.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    var recipes by mutableStateOf<List<Recipe>>(emptyList())
        private set

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            recipes = repository.getAll()
        }
    }
}

