package com.example.new2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SignInFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sign_in, container, false)

        // Initialize UI elements
        nameEditText = rootView.findViewById(R.id.signup_name)
        emailEditText = rootView.findViewById(R.id.signup_email)
        usernameEditText = rootView.findViewById(R.id.signup_username)
        passwordEditText = rootView.findViewById(R.id.signup_password)
        signupButton = rootView.findViewById(R.id.signup_btn)


        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                // Display a toast message if any of the fields is empty
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                // Check if the email is valid
                Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show()
            } else if (!isValidUsername(username)) {
                // Check if the username is valid
                Toast.makeText(requireContext(), "Invalid username", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(password)) {
                // Check if the password is valid
                Toast.makeText(requireContext(), "Invalid password", Toast.LENGTH_SHORT).show()
            } else {
                val user = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "username" to username,
                    "password" to password
                )

                usersCollection
                    .orderBy("userid", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // If no documents found, set userid to "1001"
                            user["userid"] = "1001"
                        } else {
                            // Increment the last userid value and set it as the new userid
                            val lastUser = querySnapshot.documents[0]
                            val lastUserId = lastUser.getString("userid")!!.toInt()
                            val newUserId = (lastUserId + 1).toString()
                            user["userid"] = newUserId
                        }

                        // Add the user data to Firestore
                        usersCollection
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                // Display a success toast message
                                Toast.makeText(requireContext(), "User added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()

                                // Clear input fields
                                nameEditText.text.clear()
                                emailEditText.text.clear()
                                usernameEditText.text.clear()
                                passwordEditText.text.clear()

                                // Navigate back to the login fragment
                                findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
                            }
                            .addOnFailureListener { e ->
                                // Display an error toast message if the user could not be added
                                Toast.makeText(requireContext(), "Error adding user: $e", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        // Handle Firestore query failure
                        Toast.makeText(requireContext(), "Error querying Firestore: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }


        // Find the TextView by its ID
        val signupRedirectText = rootView.findViewById<TextView>(R.id.loginRedirectText)

        // Set an OnClickListener to navigate to the SignInFragment when clicked
        signupRedirectText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
        }




        return rootView
    }

    // Function to save user data to Firestore
    private fun saveUserData(userId: String, name: String, email: String, username: String, password: String) {
        val user = hashMapOf(
            "userid" to userId,
            "name" to name,
            "email" to email,
            "username" to username,
            "password" to password
        )

        usersCollection
            .add(user)
            .addOnSuccessListener { documentReference ->
                // Display a success toast message
                Toast.makeText(
                    requireContext(),
                    "User added with ID: ${documentReference.id}",
                    Toast.LENGTH_SHORT
                ).show()

                // Clear input fields
                nameEditText.text.clear()
                emailEditText.text.clear()
                usernameEditText.text.clear()
                passwordEditText.text.clear()

                // Navigate back to the login fragment
                findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
            }
            .addOnFailureListener { e ->
                // Display an error toast message if the user could not be added
                Toast.makeText(requireContext(), "Error adding user: $e", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Function to validate username (you can define your validation rules)
    private fun isValidUsername(username: String): Boolean {
        return username.length >= 5
    }

    // Function to validate password (you can define your validation rules)
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}