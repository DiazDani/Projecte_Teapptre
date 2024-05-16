package com.jarproductions.projecte_teapptre.login

import LogInViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.databinding.ActivityLogInBinding
import com.jarproductions.projecte_teapptre.databinding.ActivitySignInBinding
import com.jarproductions.projecte_teapptre.menu.MenuActivity
import com.jarproductions.projecte_teapptre.signin.SignIn

class LogIn : AppCompatActivity() {

    private val viewModel: LogInViewModel by viewModels()
    private lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        binding.textView4.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
           startActivity(intent)
        }
        binding.button.setOnClickListener{
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            // Llamar a la función de inicio de sesión en el ViewModel
            viewModel.loginUser(email, password, this).observe(this, Observer { loginSuccessful ->
                if (loginSuccessful) {
                    // Si el inicio de sesión es exitoso, navegar a la actividad del menú
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar la actividad de inicio de sesión para evitar que el usuario vuelva atrás
                }
            })
        }
    }

    private fun setup(){

    }
}