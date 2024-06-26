package com.jarproductions.projecte_teapptre.menu.obraFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarproductions.projecte_teapptre.obraThings.Obra
import com.jarproductions.projecte_teapptre.obraThings.ObraAdapter
import com.jarproductions.projecte_teapptre.databinding.FragmentObralistBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ObrasListFragment : Fragment() {

    private lateinit var binding: FragmentObralistBinding
    private lateinit var obraAdapter: ObraAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObralistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Set up the RecyclerView
        obraAdapter = ObraAdapter(emptyList(), activity as ObraAdapter.OnObraClickListener)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = obraAdapter

        fetchObrasFromFirestore()
    }

    private fun fetchObrasFromFirestore() {
        firestore.collection("obras")
            .get()
            .addOnSuccessListener { result ->
                val obrasList = mutableListOf<Obra>()
                for (document in result) {
                    val nombre = document.getString("nombre") ?: ""
                    val fecha = document.getDate("fecha")
                    val asientosRestantes = document.getLong("asientos_restantes") ?: 0
                    if (fecha != null && fecha.after(Date()) && asientosRestantes > 0) { // Filtrar por fecha y asientos restantes
                        val fechaString = formatDate(fecha)
                        val portada = document.getString("portada") ?: "" // Obtener la URL de la portada de la obra
                        val obra = Obra(nombre, portada, 0, "", 0, fechaString) // Incluir la URL de la portada al crear el objeto Obra
                        obrasList.add(obra)
                    }
                }
                obraAdapter.setData(obrasList)
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    private fun formatDate(date: Date?): String {
        if (date == null) {
            return ""
        }
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}
