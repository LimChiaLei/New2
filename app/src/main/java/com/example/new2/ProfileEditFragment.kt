package com.example.new2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class ProfileEditFragment : Fragment() {

    // Retrieve the user's ID from SharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    private lateinit var userId: String
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.editName)
        emailEditText = view.findViewById(R.id.editEmail)
        usernameEditText = view.findViewById(R.id.editUsername)
        passwordEditText = view.findViewById(R.id.editPassword)

        // Retrieve the user's ID from SharedPreferences
        userId = sharedPreferences.getString("userId", null) ?: ""

        val saveButton = view.findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Check if all EditText fields are not blank
            if (nameEditText.text.isNotBlank() &&
                emailEditText.text.isNotBlank() &&
                usernameEditText.text.isNotBlank() &&
                passwordEditText.text.isNotBlank()
            ) {
                // Collect user input
                val inputName = nameEditText.text.toString()
                val inputEmail = emailEditText.text.toString()
                val inputUsername = usernameEditText.text.toString()
                val inputPassword = passwordEditText.text.toString()

                // Update user data in Firestore
                val db = FirebaseFirestore.getInstance()
                val userRef = db.collection("users").whereEqualTo("userid", userId)

                val userData = mapOf(
                    "name" to inputName,
                    "email" to inputEmail,
                    "username" to inputUsername,
                    "password" to inputPassword
                )

                userRef.get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val document = documents.documents[0]
                            document.reference.update(userData)
                                .addOnSuccessListener {
                                    // Redirect back to the ProfileViewFragment
                                    requireActivity().supportFragmentManager.popBackStack()
                                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(requireContext(), "User not found in Firestore", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error querying Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                    }


            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}


