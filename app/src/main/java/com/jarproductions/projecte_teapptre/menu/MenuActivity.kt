package com.jarproductions.projecte_teapptre.menu

import ObrasListFragment
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jarproductions.projecte_teapptre.Obra
import com.jarproductions.projecte_teapptre.ObraAdapter
import com.jarproductions.projecte_teapptre.ObraDetailsFragment
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.SampleFragment2
import com.jarproductions.projecte_teapptre.SampleFragment1


class MenuActivity : AppCompatActivity(), ObraAdapter.OnObraClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        button2.text = "Profile"

        button1.setOnClickListener {
            Toast.makeText(this, "Hola", Toast.LENGTH_SHORT).show()
            replaceFragment(SampleFragment1())
        }

        button2.setOnClickListener {
            Toast.makeText(this, "Caracola", Toast.LENGTH_SHORT).show()
            replaceFragment(SampleFragment2())
        }

        textViewTitle.setOnClickListener {
            replaceFragment(ObrasListFragment())
        }

        // Load the ObrasListFragment by default
        if (savedInstanceState == null) {
            replaceFragment(ObrasListFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    override fun onObraClick(obra: Obra) {
        val fragment = ObraDetailsFragment.newInstance(obra.nombre, obra.fecha.toString())
        replaceFragment(fragment)
    }
}


