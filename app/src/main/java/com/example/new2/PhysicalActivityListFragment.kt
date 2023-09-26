package com.example.new2

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class PhysicalActivityListFragment : Fragment() {
    // Retrieve the user's ID from SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_physical_activity_list, container, false)

        // Retrieve the user's ID from SharedPreferences
        val userId = sharedPreferences.getString("userId", null)

        //----------------Button------------------
        // Find the addWaterInBtn by its ID
        val backBtn = view.findViewById<Button>(R.id.backBtn)
        val addPhysicalActivityBtn = view.findViewById<Button>(R.id.addPhysicalActivityBtn)

        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_physicalActivityListFragment_to_workoutFragment)
        }

        addPhysicalActivityBtn.setOnClickListener {
            findNavController().navigate(R.id.action_physicalActivityListFragment_to_addPhysicalActivityFragment)
        }
        //----------------Button------------------

        //----------------Recycle------------------
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")
        // Query Firestore to get data where "userId" matches
        collectionRef.whereEqualTo("userId", userId).get()
            .addOnSuccessListener { documents ->
                // Initialize an ArrayList to store the retrieved data
                val physicalActivities = ArrayList<PhysicalActivity>()

                for (document in documents) {
                    // Map Firestore document data to your data model
                    val date = document.getString("date") ?: ""
                    val time = document.getString("time") ?: ""
                    val title = document.getString("title") ?: ""
                    val hours = document.getString("hours") ?: ""
                    val minute = document.getString("minute") ?: ""
                    val userId = document.getString("userId") ?: ""
                    // Get the Fires    tore document ID and set it as the id field
                    val id = document.id
                    // Create a PhysicalActivity object
                    val physicalActivity = PhysicalActivity(id, date, time, title, hours,minute,userId)
                    physicalActivities.add(physicalActivity)
                }

                // Initialize your RecyclerView adapter with the data
                val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
                val adapter = PhysicalActivityAdapter(physicalActivities)
                recyclerView.adapter = adapter

                //--------------------swipe-to-delete-------------------------
                val itemTouchHelper = ItemTouchHelper(SwipeToDeletePA(adapter, db,this))
                itemTouchHelper.attachToRecyclerView(recyclerView)
                //--------------------swipe-to-delete-------------------------
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
            }
        //----------------Recycle------------------


        return view
    }
}