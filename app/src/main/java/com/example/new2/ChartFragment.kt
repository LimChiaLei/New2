package com.example.new2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ChartFragment : Fragment() {

    // Retrieve the user's ID from SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        // Retrieve the user's ID from SharedPreferences
        val userId = sharedPreferences.getString("userId", null)

        // Find the TextView by its ID
        val testText = view.findViewById<TextView>(R.id.testtext)

        // Set the user ID as the text of the TextView
        testText.text = userId ?: "User ID not found" // Show a default message if the ID is null

        return view
    }
}
