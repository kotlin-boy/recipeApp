package com.example.recipe

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_records",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("recipeId")
    ]
)
data class MealRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val recipeId: Int,
    val date: String
)