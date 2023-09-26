package com.example.new2

import java.io.Serializable

data class PhysicalActivity(
    val id: String, // Firestore document ID
    val date: String,
    val time: String,
    val title: String,
    val hours: String,
    val minute: String,
    val userId: String,
) : Serializable
