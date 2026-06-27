package com.example.recipe

enum class Screen(
    val route: String
) {
    HOME("home"),
    ADD_RECIPE("add_recipe"),
    RECIPE_LIST("recipe_list"),
    RECIPE_DETAIL("recipe_detail/{recipeId}"),
    EDIT_RECIPE("edit_recipe")
}