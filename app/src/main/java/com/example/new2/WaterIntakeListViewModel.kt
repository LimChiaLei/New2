package com.example.new2

import androidx.lifecycle.ViewModel

class WaterIntakeListViewModel : ViewModel() {
    var waterIntakeText = ""
    var waterIntakeImage = 0 // Resource ID for the image

    fun updateWaterIntakeInfo(waterIntakeValue: Int) {
        when (waterIntakeValue) {
            60 -> {
                waterIntakeText = "60ml"
                waterIntakeImage = R.drawable.cup60
            }
            120 -> {
                waterIntakeText = "120ml"
                waterIntakeImage = R.drawable.cup120
            }
            180 -> {
                waterIntakeText = "180ml"
                waterIntakeImage = R.drawable.cup180
            }
            240 -> {
                waterIntakeText = "240ml"
                waterIntakeImage = R.drawable.cup240
            }
            else -> {
                waterIntakeText = "" // Default text when the value doesn't match any of the cases
                waterIntakeImage = 0 // Default image when the value doesn't match any of the cases
            }
        }
    }
}
