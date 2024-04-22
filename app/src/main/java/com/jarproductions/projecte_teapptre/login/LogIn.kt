package com.jarproductions.projecte_teapptre.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jarproductions.projecte_teapptre.R

class LogIn : AppCompatActivity() {

    private val viewModel: LogInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }
}