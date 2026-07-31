package com.example.recipe

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Recipe::class],
    version = 3
)
@TypeConverters(
    Converters::class
)

abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {

        fun create(
            context: Context
        ): RecipeDatabase {

            return Room.databaseBuilder(
                context,
                RecipeDatabase::class.java,
                "recipe_database"
            ).fallbackToDestructiveMigration().build()
        }
    }
}

