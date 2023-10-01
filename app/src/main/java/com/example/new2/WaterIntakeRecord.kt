package com.example.new2

import java.io.Serializable

data class WaterIntakeRecord(
    val userId: String,
    val currentDate: String,
    val currentTime: String,
    var waterIntake: Int
): Serializable
