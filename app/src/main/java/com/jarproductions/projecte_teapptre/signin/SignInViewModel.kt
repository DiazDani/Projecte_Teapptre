package com.jarproductions.projecte_teapptre.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import com.jarproductions.projecte_teapptre.repository.Repository

class SignInViewModel: ViewModel() {
    fun createUser(email: String, password: String, name: String, context: Context, confirmation:String){
        Repository.newUser(email,password,name,context,confirmation)
    }
}