package com.jarproductions.projecte_teapptre.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.databinding.ActivitySignInBinding
import androidx.activity.viewModels


class SignIn : AppCompatActivity() {
    private val viewModel: SignInViewModel by viewModels()
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }
    private fun setup(){
        binding.button.setOnClickListener{
            if (binding.editTextTextEmailAddress.text.isNotEmpty()
                && binding.editTextTextPassword.text.isNotEmpty()
                && binding.editTextText.text.isNotEmpty()
            )
        }
    }
}