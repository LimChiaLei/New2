package com.example.new2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mealPlan")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val title: String,
    val content: String,
    val imageResourceId: Int
)
