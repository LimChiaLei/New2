package com.example.new2

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhysicalActivityAddFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_physical_activity, container, false)


        //--------------Back------------------
        val backBtn = view.findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addPhysicalActivityFragment_to_physicalActivityListFragment)
        }
        //--------------Back------------------

        //--------------Menu------------------
        val addTypeBtn = view.findViewById<ImageView>(R.id.addTypeBtn)
        val workoutType = view.findViewById<EditText>(R.id.workoutType)
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

        //--------------Save Data------------------
        val addRecordBtn = view.findViewById<Button>(R.id.AddRecordBtn)
        val workoutTitle = view.findViewById<EditText>(R.id.workoutTitle)
        val workoutDate = view.findViewById<EditText>(R.id.workoutDate)
        val workoutHrs = view.findViewById<EditText>(R.id.workoutHrs)
        val workoutMins = view.findViewById<EditText>(R.id.workoutMins)
        val workoutDescription = view.findViewById<EditText>(R.id.workoutDescription)

        // Retrieve the user's ID from SharedPreferences
        val sharedPreferences: SharedPreferences by lazy {
            requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        }
        val userId = sharedPreferences.getString("userId", null)

        addRecordBtn.setOnClickListener {
            val title = workoutTitle.text.toString()
            val date = workoutDate.text.toString()
            val hours = workoutHrs.text.toString()
            val minute = workoutMins.text.toString()
            val type = workoutType.text.toString()
            val description = workoutDescription.text.toString()

            if (userId != null && title.isNotEmpty() && date.isNotEmpty() && hours.isNotEmpty() && minute.isNotEmpty()) {
                // Create a data object to save in Firestore
                val data = hashMapOf(
                    "userId" to userId,
                    "title" to title,
                    "date" to date,
                    "time" to getCurrentTime(),
                    "hours" to hours,
                    "minute" to minute,
                    "type" to type,
                    "description" to description
                )

                // Save data to Firestore under the "physicalActivity" collection
                firestore.collection("physicalActivity")
                    .add(data)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Activity recorded successfully", Toast.LENGTH_SHORT).show()
                        // Redirect to another fragment (Replace with your destination)
                        findNavController().navigate(R.id.action_addPhysicalActivityFragment_to_physicalActivityListFragment)
                    }
                    .addOnFailureListener { e ->
                        // Handle the failure to save data to Firestore
                        AlertDialog.Builder(requireContext())
                            .setTitle("Error")
                            .setMessage("Failed to save data: ${e.message}")
                            .setPositiveButton("OK", null)
                            .show()
                    }
            } else {
                // Handle invalid input, e.g., show an error message to the user
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Please fill in all the required fields.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
        //--------------Save Data------------------


        return view
    }
    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = Date()
        return timeFormat.format(currentTime)
    }
}
