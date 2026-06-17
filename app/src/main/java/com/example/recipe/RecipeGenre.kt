package com.example.recipe

enum class RecipeGenre(
    val displayName: String
) {
    MAIN("メイン"),
    SIDE_DISH("おかず"),
    SALAD("サラダ"),
    SOUP("汁物"),
    NOODLE("麺類"),
    DESSERT("デザート"),
    OTHER("その他")
}