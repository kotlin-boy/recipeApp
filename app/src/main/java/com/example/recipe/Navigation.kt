package com.example.recipe

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HOME.route
    ) {

        composable(
            route = Screen.HOME.route
        ) {
            HomeScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.ADD_RECIPE.route
        ) {
            AddRecipeScreen(
                navController = navController
            )
        }

        composable(
            route = Screen.RECIPE_LIST.route
        ) {
            RecipeViewScreen(
                navController = navController
            )
        }

        composable(
            route = "recipe_detail/{recipeId}"
        ) { backStackEntry ->

            val recipeId =
                backStackEntry.arguments
                    ?.getString("recipeId")
                    ?.toIntOrNull()
                    ?: 0

            RecipeDetailScreen(
                navController = navController,
                recipeId = recipeId
            )
        }

        composable(
            route = "edit_recipe/{recipeId}"
        ) { backStackEntry ->

            val recipeId =
                backStackEntry.arguments
                    ?.getString("recipeId")
                    ?.toIntOrNull()
                    ?: 0

            EditRecipeScreen(
                navController = navController,
                recipeId = recipeId
            )
        }
    }
}