package com.example.recipe

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Recipe::class,
        MealRecord::class
    ],
    version = 4
)
@TypeConverters(
    Converters::class
)

abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun mealRecordDao(): MealRecordDao

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

