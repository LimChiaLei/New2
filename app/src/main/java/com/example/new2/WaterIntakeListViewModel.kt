package com.example.new2

import androidx.lifecycle.ViewModel

class WaterIntakeListViewModel : ViewModel() {
    var waterIntakeText = ""
    var waterIntakeImage = 0

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
                waterIntakeText = ""
                waterIntakeImage = 0
            }
        }
    }

    // List to hold water intake records
    var waterIntakeRecords = mutableListOf<WaterIntakeRecord>()

    // Function to update or add a water intake record
    fun updateWaterIntakeRecord(record: WaterIntakeRecord) {
//        // Check if the record already exists in the list
//        val existingRecord = waterIntakeRecords.find { it.userId == record.userId && it.currentDate == record.currentDate && it.currentTime == record.currentTime }
//
//        if (existingRecord != null) {
//            // Update the existing record
//            existingRecord.waterIntake = record.waterIntake
//        } else {
//            // Add the new record to the list
//            waterIntakeRecords.add(record)
//        }
    }

    // Function to delete a water intake record
    fun deleteWaterIntakeRecord(record: WaterIntakeRecord) {
        //waterIntakeRecords.remove(record)
    }
}
