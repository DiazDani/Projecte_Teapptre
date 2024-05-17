package com.jarproductions.projecte_teapptre.menu.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jarproductions.projecte_teapptre.R

class ProfileFragment : Fragment() {
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        textViewName = view.findViewById(R.id.textViewName)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        loadUserData()
        return view
    }

    private fun loadUserData() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userEmail = user.email
            if (userEmail != null) {
                val userRef = firestore.collection("users").document(userEmail)
                userRef.get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        if (name != null && email != null) {
                            textViewName.text = name
                            textViewEmail.text = email
                        } else {
                            // Handle null values
                            Log.e("com.jarproductions.projecte_teapptre.menu.profile.ProfileFragment", "Name or email is null")
                        }
                    } else {
                        // Document doesn't exist
                        Log.e("com.jarproductions.projecte_teapptre.menu.profile.ProfileFragment", "Document does not exist for user: ${user.uid}")
                    }
                }.addOnFailureListener { exception ->
                    // Handle error
                    Log.e("com.jarproductions.projecte_teapptre.menu.profile.ProfileFragment", "Error fetching document: $exception")
                }
            } else {
                // User email is null
                Log.e("com.jarproductions.projecte_teapptre.menu.profile.ProfileFragment", "User email is null")
            }
        } ?: run {
            // Current user is null
            Log.e("com.jarproductions.projecte_teapptre.menu.profile.ProfileFragment", "Current user is null")
        }
    }


}
