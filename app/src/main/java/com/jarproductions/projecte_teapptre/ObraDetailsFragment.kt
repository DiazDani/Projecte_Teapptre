package com.jarproductions.projecte_teapptre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jarproductions.projecte_teapptre.databinding.FragmentObradetailsBinding

class ObraDetailsFragment : Fragment() {

    private lateinit var binding: FragmentObradetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObradetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nombre = arguments?.getString("nombre") ?: ""
        val fecha = arguments?.getString("fecha") ?: ""

        binding.titleTextView.text = nombre
        binding.theaterNameTextView.text = fecha // Ajusta esto según tu layout

        // Configura otros elementos de tu layout si es necesario
    }

    companion object {
        fun newInstance(nombre: String, fecha: String): ObraDetailsFragment {
            val fragment = ObraDetailsFragment()
            val args = Bundle()
            args.putString("nombre", nombre)
            args.putString("fecha", fecha)
            fragment.arguments = args
            return fragment
        }
    }
}

