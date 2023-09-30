package com.example.new2

data class WaterIntakeRecord(
    val userId: String,
    val currentDate: String,
    val currentTime: String,
    var waterIntake: Int
)
