package com.jarproductions.projecte_teapptre.menu.obraFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.jarproductions.projecte_teapptre.databinding.FragmentObradetailsBinding

class ObraDetailsFragment : Fragment() {

    private lateinit var binding: FragmentObradetailsBinding
    private val firestore = FirebaseFirestore.getInstance()

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

        // Realizar la consulta a la base de datos para obtener la descripción y los asientos restantes
        firestore.collection("obras")
            .whereEqualTo("nombre", nombre) // Cambia "nombre" al nombre del campo en tu base de datos
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val descripcion = document.getString("descripcion")
                    val asientosRestantes = document.getLong("asientos_restantes")

                    // Establecer los valores en los campos correspondientes
                    binding.descTextView.text = descripcion
                    binding.seatsLeftTextView.text = "Asientos restantes: $asientosRestantes"
                }
            }
            .addOnFailureListener { exception ->
                // Manejar errores aquí
            }
    }

    companion object {
        fun newInstance(
            nombre: String,
            fecha: String
        ): ObraDetailsFragment {
            val fragment = ObraDetailsFragment()
            val args = Bundle()
            args.putString("nombre", nombre)
            args.putString("fecha", fecha)
            fragment.arguments = args
            return fragment
        }
    }
}
