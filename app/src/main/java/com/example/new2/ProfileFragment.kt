package com.example.new2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    // Retrieve the user's ID from SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    //bottom
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)



        // Retrieve the user's ID from SharedPreferences
        val userId = sharedPreferences.getString("userId", null)

        if (userId != null) {
            // Initialize Firestore
            val db = FirebaseFirestore.getInstance()

            // Reference to the "users" collection
            val usersCollection = db.collection("users")

            // Query to get the user data based on userId
            usersCollection.whereEqualTo("userid", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Get user data
                        val name = document.getString("name")
                        val email = document.getString("email")

                        // Update TextViews

                        view.findViewById<TextView>(R.id.userName).text = name
                        view.findViewById<TextView>(R.id.gmail).text = email
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle errors here, you can log or display a message to the user
                    Log.e("ProfileViewFragment", "Error getting user data", exception)
                    Toast.makeText(requireContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                }
        }

        // Find the ImgView by its ID
        val profilePicImageView = view.findViewById<ImageView>(R.id.profilePic)
        val chartFeild = view.findViewById<View>(R.id.chartFeild)
        val chartIcon = view.findViewById<View>(R.id.chartIcon)
        val right2 = view.findViewById<View>(R.id.right2)

        // Set an OnClickListener for the chartFeild
        chartFeild.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_stepCountRecordFragment)
        }

        // Set an OnClickListener for the chartIcon (if needed)
        chartIcon.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_stepCountRecordFragment)
        }

        // Set an OnClickListener for the right2 (if needed)
        right2.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_stepCountRecordFragment)
        }


        // Set an OnClickListener to navigate to ProfileViewFragment
        profilePicImageView.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_viewProfileFragment)
        }

        // Set up the BottomNavigationView
        val bottomNavView = view?.findViewById<BottomNavigationView>(R.id.bottomNavView)
        if (bottomNavView != null) {
            bottomNavView.selectedItemId = R.id.profile
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the NavController
        navController = requireActivity().findNavController(R.id.nav_host_fragment)

        // Set up the BottomNavigationView
        val bottomNavView = view.findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Navigate to HomeFragment
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.workout -> {
                    // Navigate to WorkoutFragment
                    navController.navigate(R.id.workoutFragment)
                    true
                }
                R.id.chart -> {
                    // Navigate to ChartFragment
                    navController.navigate(R.id.chartFragment)
                    true
                }
                R.id.profile -> {
                    true
                }
                else -> false
            }
        }
    }
}