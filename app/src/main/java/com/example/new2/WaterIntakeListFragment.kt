package com.example.new2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WaterIntakeListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WaterIntakeAdapter

    // Retrieve the user's ID from SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_water_intake_list, container, false)

        // Retrieve the user's ID from SharedPreferences
        val userId = sharedPreferences.getString("userId", null)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns for grid

        // Create an empty list for water intake records
        val waterIntakeRecordLists = mutableListOf<WaterIntakeRecord>()

        // Initialize Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Create a Firestore query to fetch records for the specific userId
        val query = db.collection("waterIntake")
            .whereEqualTo("userId", userId)

        // Fetch data from Firestore
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = query.get().await()
                val waterIntakeRecordLists = mutableListOf<WaterIntakeRecord>() // Initialize the list here

                for (document in result.documents) {
                    val userId = document.getString("userId") ?: ""
                    val currentDate = document.getString("currentDate") ?: ""
                    val currentTime = document.getString("currentTime") ?: ""
                    val waterIntake = document.getLong("waterIntake")?.toInt() ?: 0

                    val record = WaterIntakeRecord(userId, currentDate, currentTime, waterIntake)
                    waterIntakeRecordLists.add(record)
                }

                // Sort the list in descending order based on date and time
                waterIntakeRecordLists.sortByDescending { it.currentDate + " " + it.currentTime }

                // Update the UI with the sorted data
                launch(Dispatchers.Main) {
                    adapter = WaterIntakeAdapter(waterIntakeRecordLists)
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                // Handle any errors
                e.printStackTrace()
            }
        }


        // Find the addWaterInBtn by its ID
        val backBtn = view.findViewById<Button>(R.id.backBtn)
        val addWaterInBtn = view.findViewById<Button>(R.id.addWaterInBtn)

        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_waterIntakeListFragment_to_workoutFragment)
        }

        addWaterInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_waterIntakeListFragment_to_addWaterIntakeFragment)
        }

        return view
    }
}
