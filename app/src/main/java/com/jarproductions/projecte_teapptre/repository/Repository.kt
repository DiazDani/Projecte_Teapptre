package com.jarproductions.projecte_teapptre.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.jarproductions.projecte_teapptre.Obra
import com.jarproductions.projecte_teapptre.User
import com.jarproductions.projecte_teapptre.login.LogIn
import kotlinx.coroutines.tasks.await


class Repository {


    companion object {
        val db = Firebase.firestore

        fun loginUser(email: String, password: String, context: Context): LiveData<Boolean> {
            val auth = FirebaseAuth.getInstance()

            val loginResult = MutableLiveData<Boolean>()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Inicio de sesión exitoso
                        loginResult.postValue(true)
                    } else {
                        // Inicio de sesión fallido
                        Toast.makeText(
                            context,
                            "Inicio de sesión fallido. Verifique sus credenciales e inténtelo de nuevo.",
                            Toast.LENGTH_SHORT
                        ).show()
                        loginResult.postValue(false)
                    }
                }

            return loginResult
        }
        fun newUser(
            email: String,
            password: String,
            name: String,
            context: Context,
            confirmPasword: String
        ) {
            if (confirmPasword == password) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "El registre no ha funcionat!",
                                Toast.LENGTH_SHORT
                            ).show()
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


}