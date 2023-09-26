//package com.example.new2
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//
//@Dao
//interface MealPlanDao {
//    @Insert
//    suspend fun insertMealPlan(mealPlan: MealPlanEntity)
//
//    @Query("SELECT * FROM mealPlan")
//    fun readAll(): Flow<List<MealPlanEntity>>
//}
