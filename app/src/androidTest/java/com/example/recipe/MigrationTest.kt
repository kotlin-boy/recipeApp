package com.example.recipe

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val testDb = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        RecipeDatabase::class.java
    )

    @Test
    fun migrate3To4_keepsRecipeAndCreatesMealRecordsTable() {
        //Version 3 のDBを作成
        helper.createDatabase(testDb, 3).apply {

            //Version 3 のDBに既存レシピがある状態を再現
            execSQL(
                """
                INSERT INTO Recipe
                    (id, name, genre, ingredients, steps, memo, isFavorite, imageUri)
                VALUES
                    (1, 'テストレシピ', 'TEST', '[]', '[]', 'テストメモ', 0, '')
                """.trimIndent()
            )

            close()
        }

        //Version 3 → Version 4 にMigration
        val db = helper.runMigrationsAndValidate(
            testDb,
            4,
            true,
            RecipeDatabase.MIGRATION_3_4
        )

        //既存レシピが消えていないことを確認
        db.query(
            "SELECT name, memo FROM Recipe WHERE id = 1"
        ).use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("テストレシピ", cursor.getString(0))
            assertEquals("テストメモ", cursor.getString(1))
        }

        //Version 4で追加されたmeal_recordsが使用できることを確認
        db.execSQL(
            """
            INSERT INTO meal_records (recipeId, date)
            VALUES (1, '2026-10-06')
            """.trimIndent()
        )

        db.query(
            "SELECT recipeId, date FROM meal_records"
        ).use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(0))
            assertEquals("2026-10-06", cursor.getString(1))
        }

        db.close()
    }
}