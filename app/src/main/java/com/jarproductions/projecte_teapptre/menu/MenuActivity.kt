package com.jarproductions.projecte_teapptre.menu

import ReservationFragment
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jarproductions.projecte_teapptre.obraThings.Obra
import com.jarproductions.projecte_teapptre.obraThings.ObraAdapter
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.menu.obraFragments.ObraDetailsFragment
import com.jarproductions.projecte_teapptre.menu.obraFragments.ObrasListFragment
import com.jarproductions.projecte_teapptre.menu.profile.ProfileFragment



class MenuActivity : AppCompatActivity(), ObraAdapter.OnObraClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val textViewTitle: TextView = findViewById(R.id.textViewTitle)
        button2.text = "Profile"
        button1.text = "Check Reservations"


        button1.setOnClickListener {
            replaceFragment(ReservationFragment())
        }

        button2.setOnClickListener {
            replaceFragment(ProfileFragment())
        }

        textViewTitle.setOnClickListener {
            replaceFragment(ObrasListFragment())
        }

        // Load the com.jarproductions.projecte_teapptre.menu.obraFragments.ObrasListFragment by default
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
        val fragment = ObraDetailsFragment.newInstance(
            obra.nombre,
            obra.portada ?: " "
        )
        replaceFragment(fragment)
    }

}


