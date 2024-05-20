package com.jarproductions.projecte_teapptre.menu.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.login.LogIn

class ProfileFragment : Fragment() {
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var imageView: ImageView
    private lateinit var buttonLogout: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        textViewName = view.findViewById(R.id.textViewName)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        imageView = view.findViewById(R.id.imageView)
        buttonLogout = view.findViewById(R.id.buttonLogout)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        loadUserData()
        setupLogoutButton()
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
                        val fotoUrl = document.getString("foto")
                        if (name != null && email != null && fotoUrl != null) {
                            textViewName.text = name
                            textViewEmail.text = email
                            Glide.with(this)
                                .load(fotoUrl)
                                .circleCrop()
                                .into(imageView)
                        } else {
                            Log.e("ProfileFragment", "Name, email, or fotoUrl is null")
                        }
                    } else {
                        Log.e("ProfileFragment", "Document does not exist for user: $userEmail")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("ProfileFragment", "Error fetching document: $exception")
                }
            } else {
                Log.e("ProfileFragment", "User email is null")
            }
        } ?: run {
            Log.e("ProfileFragment", "Current user is null")
        }
    }

    private fun setupLogoutButton() {
        buttonLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, LogIn::class.java)
            startActivity(intent)
            activity?.finish() // Close the current activity
        }
    }
}
