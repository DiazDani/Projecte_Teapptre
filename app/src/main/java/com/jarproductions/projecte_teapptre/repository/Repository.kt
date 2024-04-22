package com.jarproductions.projecte_teapptre.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.jarproductions.projecte_teapptre.login.LogIn


class Repository {
    companion object{
        val db = Firebase.firestore
        fun newUser(email: String, password: String, name: String, context: Context,confirmPasword: String) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(context, "El registre no ha funcionat!", Toast.LENGTH_SHORT).show()
                    } else {
                        val userDocument = db.collection("users").document(email)

                        val userData = hashMapOf(
                            "name" to name,
                            "email" to email,
                        )

                        userDocument.set(userData)
                        Toast.makeText(context, "Registre realitzat", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, LogIn::class.java)
                        context.startActivity(intent)
                    }
                }
        }
    }



}