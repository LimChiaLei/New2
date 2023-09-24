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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.new2.R
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        // Initialize UI elements
        usernameEditText = rootView.findViewById(R.id.signup_username)
        passwordEditText = rootView.findViewById(R.id.signup_password)
        loginButton = rootView.findViewById(R.id.login_btn)

        // Set an OnClickListener for the Login button
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                // Display a toast message if any of the fields is empty
                Toast.makeText(requireContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Check if the user exists in the Firestore "users" collection
                usersCollection
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            // User exists with the provided username and password
                            val user = querySnapshot.documents[0] // Assuming only one user can match the criteria
                            // Save the user's ID in SharedPreferences
                            val userId = user.getString("userid")

                            if (userId != null) {
                                saveUserId(userId)
                                Log.d("LoginFragment", "User ID saved: $userId")
                                // User exists with the provided username and password, navigate to the home fragment
                                findNavController().navigate(R.id.action_loginFragment_to_workoutFragment)
                            } else {
                                // Handle the case where the "userId" field is not found in the document
                                Toast.makeText(
                                    requireContext(),
                                    "User ID not found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // No user found with the provided credentials
                            Toast.makeText(
                                requireContext(),
                                "Wrong username or password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Error occurred while checking the database
                        Toast.makeText(
                            requireContext(),
                            "Error: $e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }


        // Find the TextView by its ID
        val signupRedirectText = rootView.findViewById<TextView>(R.id.signupRedirectText)

        // Set an OnClickListener to navigate to the SignInFragment when clicked
        signupRedirectText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }
        return rootView
    }
    // Function to save the user's ID in SharedPreferences
    private fun saveUserId(userId: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }
}

