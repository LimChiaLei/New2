package com.example.new2

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterIntakeAddFragment : Fragment() {

    private lateinit var seekBar: SeekBar
    private lateinit var valueTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_water_intake, container, false)

        // Find the addWaterInBtn by its ID
        val backBtn = view.findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addWaterIntakeFragment_to_waterIntakeListFragment)
        }

        // Initialize your views here
        seekBar = view.findViewById(R.id.seekBar)
        valueTextView = view.findViewById(R.id.valuetxt)
        val cupImg = view.findViewById<ImageView>(R.id.cupImg)

        seekBar.max = 4  // Set the maximum value

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Map the progress to your custom values
                val customValues = intArrayOf(0, 60, 120, 180, 240)
                valueTextView.text = customValues[progress].toString()

                // Update the cupImg based on progress
                when (customValues[progress]) {
                    0 -> cupImg.setImageResource(R.drawable.cup0)
                    60 -> cupImg.setImageResource(R.drawable.cup60)
                    120 -> cupImg.setImageResource(R.drawable.cup120)
                    180 -> cupImg.setImageResource(R.drawable.cup180)
                    240 -> cupImg.setImageResource(R.drawable.cup240)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not needed for this example
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not needed for this example
            }
        })


        // Find the "drinked" button by its ID
        val drinkedButton = view.findViewById<Button>(R.id.AddRecordBtn)

        // Set up a click listener for the "drinked" button
        drinkedButton.setOnClickListener {
            // Get the selected water intake value
            val waterIntakeValue = valueTextView.text.toString().toInt()

            // Retrieve the user's ID from SharedPreferences (as you mentioned)
            val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", null)

            // If userId is not null, store the data in Firestore
            if (userId != null) {
                val waterIntakeRecord = WaterIntakeRecord(userId, getCurrentDate(), getCurrentTime(), waterIntakeValue)

                // Get a reference to your Firestore database
                val db = FirebaseFirestore.getInstance()

                // Add the record to the "water intake" collection
                db.collection("waterIntake")
                    .add(waterIntakeRecord)
                    .addOnSuccessListener { documentReference ->
                        // The record was successfully added
                        Log.d(TAG, "Water intake record added with ID: ${documentReference.id}")
                        // Show a success message (you can use a Toast or Snackbar)
                        Toast.makeText(requireContext(), "Water intake recorded successfully", Toast.LENGTH_SHORT).show()

                        // Redirect to the list fragment
                        findNavController().navigate(R.id.action_addWaterIntakeFragment_to_waterIntakeListFragment)
                    }
                    .addOnFailureListener { e ->
                        // Handle any errors that occurred while adding the record
                        Log.w(TAG, "Error adding water intake record", e)
                        // Show an error message (you can use a Toast or Snackbar)
                        Toast.makeText(requireContext(), "Failed to record water intake", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        return view
    }
    // Functions for getting current date and time
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = Date()
        return timeFormat.format(currentTime)
    }
}
