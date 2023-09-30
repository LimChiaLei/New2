package com.example.new2

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PhysicalActivityEditFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_physical_activity, container, false)

        val backBtn = view.findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_editPhysicalActivityFragment_to_physicalActivityListFragment)
        }

        // Retrieve the passed data (PhysicalActivity object) from the arguments
        val physicalActivity = arguments?.getSerializable("physicalActivity") as PhysicalActivity
        //set user id from recycle view
        val userIdGet = physicalActivity.userId
        val dateGet = physicalActivity.date
        val timeGet = physicalActivity.time

        // Now, you can populate your UI elements with the data
        val workoutTitle = view.findViewById<TextView>(R.id.workoutTitle)
        val workoutDate = view.findViewById<TextView>(R.id.workoutDate)
        val workoutHrs = view.findViewById<TextView>(R.id.workoutHrs)
        val workoutMins = view.findViewById<TextView>(R.id.workoutMins)
        val workoutType = view.findViewById<TextView>(R.id.workoutType)
        val workoutDescription = view.findViewById<TextView>(R.id.workoutDescription)
        val updateAddRecordBtn = view.findViewById<Button>(R.id.updateAddRecordBtn)

        // Create a reference to the "physicalActivity" collection
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("physicalActivity")

        // Query Firestore to get the specific document

        //--------------get data for edit------------------------------
        collectionRef
            .whereEqualTo("userId", userIdGet)
            .whereEqualTo("date", dateGet)
            .whereEqualTo("time", timeGet)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Retrieve data from the document
                    val title = document.getString("title") ?: ""
                    val date = document.getString("date") ?: ""
                    val hours = document.getString("hours") ?: ""
                    val minute = document.getString("minute") ?: ""
                    val type = document.getString("type") ?: ""
                    val description = document.getString("description") ?: ""

                    // Now you can store the data in variables
                    val storedTitle = title
                    val storedDate = date
                    val storedHours = hours
                    val storedMinute = minute
                    val storedType = type
                    val storedDescription = description

                    workoutTitle.text = storedTitle
                    workoutDate.text = storedDate
                    workoutHrs.text = storedHours
                    workoutMins.text = storedMinute
                    workoutType.text = storedType
                    workoutDescription.text = storedDescription
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(TAG, "Error getting documents: ", exception)
            }
        //--------------get data for edit------------------------------

        //--------------Menu------------------
        val addTypeBtn = view.findViewById<ImageView>(R.id.addTypeBtn)
        // Initialize a PopupMenu
        val popupMenu = PopupMenu(requireContext(), addTypeBtn)
        popupMenu.menuInflater.inflate(R.menu.physical_item_list, popupMenu.menu)

        // Set a click listener for each menu item
        popupMenu.setOnMenuItemClickListener { menuItem ->
            // Handle menu item click here
            when (menuItem.itemId) {
                R.id.op1 -> {
                    // Handle op1 click
                    workoutType.setText("Running")
                    addTypeBtn.setImageResource(R.drawable.pa_running)
                    true
                }
                R.id.op2 -> {
                    // Handle op2 click
                    workoutType.setText("Waking")
                    addTypeBtn.setImageResource(R.drawable.pa_walking)
                    true
                }
                R.id.op3 -> {
                    // Handle op3 click
                    workoutType.setText("Cycling")
                    addTypeBtn.setImageResource(R.drawable.pa_cycling)
                    true
                }
                R.id.op4 -> {
                    // Handle op3 click
                    workoutType.setText("Swimming")
                    addTypeBtn.setImageResource(R.drawable.pa_swimming)
                    true
                }
                R.id.op5 -> {
                    // Handle op3 click
                    workoutType.setText("Sport")
                    addTypeBtn.setImageResource(R.drawable.pa_sport)
                    true
                }
                R.id.op6 -> {
                    // Handle op3 click
                    workoutType.setText("Yoga")
                    addTypeBtn.setImageResource(R.drawable.pa_yoga)
                    true
                }
                R.id.op7 -> {
                    // Handle op3 click
                    workoutType.setText("Gym")
                    addTypeBtn.setImageResource(R.drawable.pa_gym)
                    true
                }
                else -> false
            }
        }

        addTypeBtn.setOnClickListener {
            popupMenu.show()
        }
        //--------------Menu------------------


        //--------------update data for edit------------------------------
        updateAddRecordBtn.setOnClickListener {
            val title = workoutTitle.text.toString()
            val date = workoutDate.text.toString()
            val hours = workoutHrs.text.toString()
            val minute = workoutMins.text.toString()
            val type = workoutType.text.toString()
            val description = workoutDescription.text.toString()

            // Assuming you have the `physicalActivity` object with userId, date, and time properties
            val query = firestore.collection("physicalActivity")
                .whereEqualTo("userId", userIdGet)
                .whereEqualTo("date", dateGet)
                .whereEqualTo("time", timeGet)

            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Update the fields with user input
                        document.reference.update(
                            "title", title,
                            "date", date,
                            "time",getCurrentTime(),
                            "hours", hours,
                            "minute", minute,
                            "type", type,
                            "description", description
                        ).addOnSuccessListener {
                            // Data updated successfully, show a success message
                            Toast.makeText(
                                requireContext(),
                                "Data updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Redirect to the previous fragment
                            findNavController().navigateUp()
                        }.addOnFailureListener { exception ->
                            // Handle the update error
                            Log.e(TAG, "Error updating document", exception)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the query error
                    Log.e(TAG, "Error querying document", exception)
                }
            //--------------update data for edit------------------------------
        }
        return view
    }
    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = Date()
        return timeFormat.format(currentTime)
    }
}