package com.example.recipe

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


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

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    """
                CREATE TABLE IF NOT EXISTS `meal_records` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `recipeId` INTEGER NOT NULL,
                    `date` TEXT NOT NULL,
                    FOREIGN KEY(`recipeId`) REFERENCES `Recipe`(`id`)
                        ON UPDATE NO ACTION
                        ON DELETE CASCADE
                )
                """.trimIndent()
                )

                db.execSQL(
                    """
                CREATE INDEX IF NOT EXISTS `index_meal_records_recipeId`
                ON `meal_records` (`recipeId`)
                """.trimIndent()
                )
            }
        }

        fun create(
            context: Context
        ): RecipeDatabase {

            return Room.databaseBuilder(
                context,
                RecipeDatabase::class.java,
                "recipe_database"
            )
                .addMigrations(MIGRATION_3_4)
                .build()
        }
    }
}



