package com.jarproductions.projecte_teapptre.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.databinding.ActivitySignInBinding
import androidx.activity.viewModels
import com.jarproductions.projecte_teapptre.login.LogIn


class SignIn : AppCompatActivity() {
    private val viewModel: SignInViewModel by viewModels()
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
        binding.textView4.setOnClickListener{
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }
    }
    private fun setup(){
        binding.button.setOnClickListener{
            if (binding.editTextTextEmailAddress.text.isNotEmpty()
                && binding.editTextTextPassword.text.isNotEmpty()
                && binding.editTextText.text.isNotEmpty()
                && binding.editTextTextPassword2.text.isNotEmpty())
            {
                viewModel.createUser(
                    binding.editTextTextEmailAddress.text.toString(),
                    binding.editTextTextPassword.text.toString(),
                    binding.editTextText.text.toString(),
                    this,
                    binding.editTextTextPassword2.text.toString()
                )
            }
        }
    }
}