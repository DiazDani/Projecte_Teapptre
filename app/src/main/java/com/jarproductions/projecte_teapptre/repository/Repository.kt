package com.jarproductions.projecte_teapptre.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class Repository {
    companion object{
        val db = Firebase.firestore
    }
}