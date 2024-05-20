package com.jarproductions.projecte_teapptre.menu.obraFragments

import ReservationFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.databinding.FragmentObradetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ObraDetailsFragment : Fragment() {

    private lateinit var binding: FragmentObradetailsBinding
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObradetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Get the arguments passed to this fragment
        val nombre = arguments?.getString("nombre") ?: ""
        val portada = arguments?.getString("portada") ?: ""

        // Listener for the cancel reservation button
        binding.cancelReservationButton.setOnClickListener {
            cancelReservation(nombre)
        }

        // Listener for the pick seat button
        binding.pickSeatButton.setOnClickListener {
            reserveSeat(nombre)
        }

        // Fetch obra details from Firestore
        firestore.collection("obras")
            .document(nombre)
            .get()
            .addOnSuccessListener { document ->
                val nombreObra = document.getString("nombre") ?: ""
                val fechaTimestamp = document.getTimestamp("fecha")
                val fecha = if (fechaTimestamp != null) {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    dateFormat.format(fechaTimestamp.toDate())
                } else {
                    ""
                }

                binding.titleTextView.text = nombreObra
                binding.theaterNameTextView.text = fecha
                val descripcion = document.getString("descripcion")
                val asientosRestantes = document.getLong("asientos_restantes")
                val portadaUrl = document.getString("portada")
                binding.descTextView.text = descripcion
                binding.seatsLeftTextView.text = "Seients restants: $asientosRestantes"

                // Load the image using Glide
                if (portadaUrl != null) {
                    Glide.with(this)
                        .load(portadaUrl)
                        .placeholder(null)
                        .into(binding.largeImage)
                } else {
                    // Handle null or empty URL
                }

                // Check if the current user has reserved this obra
                val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
                firestore.collection("users")
                    .document(currentUserEmail)
                    .get()
                    .addOnSuccessListener { document ->
                        val userReservas = document.get("reservas") as? List<Map<String, Any>> ?: emptyList()
                        val yaReservada = userReservas.any { it["reserva"] == nombre }
                        if (yaReservada) {
                            binding.cancelReservationButton.visibility = View.VISIBLE
                            binding.pickSeatButton.text = "Ja reservat"
                            binding.pickSeatButton.setOnClickListener{
                                Toast.makeText(context, "Ja ha reservat un seient!", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            binding.cancelReservationButton.visibility = View.GONE
                            binding.pickSeatButton.text = "Reservar seient"
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }

    private fun reserveSeat(obraNombre: String) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        firestore.collection("users")
            .document(currentUserEmail)
            .get()
            .addOnSuccessListener { document ->
                val userReservas = document.get("reservas") as? List<Map<String, Any>> ?: emptyList()
                val yaReservada = userReservas.any { it["reserva"] == obraNombre }
                if (!yaReservada) {
                    firestore.collection("obras")
                        .document(obraNombre)
                        .get()
                        .addOnSuccessListener { document ->
                            val asientosRestantes = document.getLong("asientos_restantes") ?: 0
                            val asientosTotales = document.getLong("asientos") ?: 0
                            val reservados = document.get("reservados") as? MutableList<Int> ?: mutableListOf()
                            if (asientosRestantes > 0) {
                                var numeroAsiento: Int
                                do {
                                    numeroAsiento = Random.nextInt(1, asientosTotales.toInt() + 1)
                                } while (reservados.contains(numeroAsiento))
                                reservados.add(numeroAsiento)
                                firestore.collection("obras")
                                    .document(obraNombre)
                                    .update(
                                        mapOf(
                                            "reservados" to reservados,
                                            "asientos_restantes" to asientosRestantes - 1
                                        )
                                    )
                                    .addOnSuccessListener {
                                        // Actualizar datos del usuario
                                        updateUserReservation(numeroAsiento, obraNombre)
                                        Toast.makeText(context, "Reserva Realitzada", Toast.LENGTH_SHORT).show()

                                        // Navigate to ReservationFragment
                                        val fragment = ObrasListFragment()
                                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, fragment)?.commit()
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle errors
                                    }
                            }
                        }
                }
            }
    }

    private fun updateUserReservation(asiento: Int, obraNombre: String) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        firestore.collection("users")
            .document(currentUserEmail)
            .get()
            .addOnSuccessListener { document ->
                val userReservas = document.get("reservas") as? MutableList<Map<String, Any>> ?: mutableListOf()
                userReservas.add(mapOf("reserva" to obraNombre, "asiento" to asiento))
                firestore.collection("users")
                    .document(currentUserEmail)
                    .update("reservas", userReservas)
                    .addOnSuccessListener {
                        // Handle successful reservation
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }
    }

    private fun cancelReservation(obraNombre: String) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        firestore.collection("users")
            .document(currentUserEmail)
            .get()
            .addOnSuccessListener { document ->
                val userReservas = document.get("reservas") as? MutableList<Map<String, Any>> ?: mutableListOf()
                val reserva = userReservas.find { it["reserva"] == obraNombre }
                if (reserva != null) {
                    val asientoReservado = (reserva["asiento"] as? Long)?.toInt() ?: return@addOnSuccessListener
                    userReservas.remove(reserva)

                    firestore.collection("users")
                        .document(currentUserEmail)
                        .update("reservas", userReservas)
                        .addOnSuccessListener {
                            updateObraSeats(obraNombre, asientoReservado)
                        }
                        .addOnFailureListener { exception ->
                            // Handle errors
                        }
                }
            }
    }

    private fun updateObraSeats(obraNombre: String, asientoReservado: Int) {
        firestore.collection("obras")
            .document(obraNombre)
            .get()
            .addOnSuccessListener { document ->
                val asientosRestantes = document.getLong("asientos_restantes") ?: 0
                val reservados = document.get("reservados") as? MutableList<Int> ?: mutableListOf()
                reservados.remove(asientoReservado)

                firestore.collection("obras")
                    .document(obraNombre)
                    .update(
                        mapOf(
                            "reservados" to reservados,
                            "asientos_restantes" to asientosRestantes + 1
                        )
                    )
                    .addOnSuccessListener {
                        // Handle successful reservation cancellation
                        Toast.makeText(context, "Reserva cancelada", Toast.LENGTH_SHORT).show()
                        val fragment = ObrasListFragment()
                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, fragment)?.commit()
                    }
                    .addOnFailureListener { exception ->
                        // Handle errors
                    }
            }
    }

    private fun formatDate(date: Date?): String {
        if (date == null) {
            return ""
        }
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    companion object {
        fun newInstance(
            nombre: String,
            portada: String
        ): ObraDetailsFragment {
            val fragment = ObraDetailsFragment()
            val args = Bundle()
            args.putString("nombre", nombre)
            args.putString("portada", portada)
            fragment.arguments = args
            return fragment
        }
    }

}

