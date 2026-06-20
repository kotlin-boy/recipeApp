package com.example.recipe

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromGenre(
        genre: RecipeGenre
    ): String {
        return genre.name
    }

    @TypeConverter
    fun toGenre(
        value: String
    ): RecipeGenre {
        return RecipeGenre.valueOf(value)
    }

    @TypeConverter
    fun fromStringList(
        list: List<String>
    ): String {
        return list.joinToString("|")
    }

    @TypeConverter
    fun toStringList(
        value: String
    ): List<String> {
        return value.split("|")
    }
}