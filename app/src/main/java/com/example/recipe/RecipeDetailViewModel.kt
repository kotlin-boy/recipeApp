package com.example.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeDatailViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    var recipe by mutableStateOf<Recipe?>(null)
        private set

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            recipe = repository.getRecipeById(id)
        }
    }

    suspend fun deleteRecipe() {
        val recipe = recipe ?: return
        repository.delete(recipe)
    }
}