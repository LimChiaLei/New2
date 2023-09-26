package com.example.new2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore

class ViewProfileFragment : Fragment() {

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_profile, container, false)

        val backBtn = view.findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_viewProfileFragment_to_profileFragment)
        }

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
                        val username = document.getString("username")
                        val password = document.getString("password")

                        // Update TextViews
                        view.findViewById<TextView>(R.id.titleName).text = name
                        view.findViewById<TextView>(R.id.titleUsername).text = username
                        view.findViewById<TextView>(R.id.profileName).text = name
                        view.findViewById<TextView>(R.id.profileEmail).text = email
                        view.findViewById<TextView>(R.id.profileUsername).text = username
                        view.findViewById<TextView>(R.id.profilePassword).text = password
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle errors here, you can log or display a message to the user
                    Log.e("ViewProfileFragment", "Error getting user data", exception)
                    Toast.makeText(requireContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                }
        }


        // Find the Button by its ID
        val editProfileBtn = view.findViewById<Button>(R.id.editButton)

        // Set an OnClickListener to navigate to ViewProfileFragment
        editProfileBtn.setOnClickListener {
            // Navigate to the ViewProfileFragment
            findNavController().navigate(R.id.action_viewProfileFragment_to_editProfileFragment)
        }


        return view
    }
}
