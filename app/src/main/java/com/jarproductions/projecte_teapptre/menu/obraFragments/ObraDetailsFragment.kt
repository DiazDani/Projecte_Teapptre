import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.databinding.FragmentObradetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

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


        // Listener para el botón de reservar asiento
        binding.pickSeatButton.setOnClickListener {
            reserveSeat(nombre)
        }

        // Realizar la consulta a la base de datos para obtener la descripción y los asientos restantes
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
                binding.seatsLeftTextView.text = "Asientos restantes: $asientosRestantes"

                // Cargar la imagen de la portada utilizando Glide
                if (portadaUrl != null) {
                    Glide.with(this)
                        .load(portadaUrl)
                        .placeholder(null) // Elimina el placeholder
                        .into(binding.largeImage)
                } else {
                    // Manejar caso de URL de portada nula
                }
            }
            .addOnFailureListener { exception ->
                // Manejar errores aquí
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
                if (yaReservada) {
                    // Manejar el caso en que el usuario ya ha reservado esta obra
                } else {
                    firestore.collection("obras")
                        .document(obraNombre)
                        .get()
                        .addOnSuccessListener { document ->
                            val asientosRestantes = document.getLong("asientos_restantes") ?: 0
                            if (asientosRestantes > 0) {
                                val numeroAsiento = Random.nextInt(1, asientosRestantes.toInt() + 1)
                                val reservados = document.get("reservados") as? MutableList<Int> ?: mutableListOf()
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
                                    }
                                    .addOnFailureListener { exception ->
                                        // Manejar errores aquí
                                    }
                            } else {
                                // Manejar el caso en que no haya asientos disponibles
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Manejar errores aquí
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Manejar errores aquí
            }
    }


    private fun updateUserReservation(numeroAsiento: Int, obraNombre: String) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        firestore.collection("users")
            .document(currentUserEmail)
            .get()
            .addOnSuccessListener { document ->
                val userReservas = document.get("reservas") as? MutableList<HashMap<String, Any>> ?: mutableListOf()
                val nuevaReserva: HashMap<String, Any> = hashMapOf(
                    "asiento" to numeroAsiento,
                    "reserva" to obraNombre
                )

                userReservas.add(nuevaReserva)
                firestore.collection("users")
                    .document(currentUserEmail)
                    .update("reservas", userReservas)
                    .addOnSuccessListener {
                        // Navegar al fragmento de reservas
                        val fragment = ReservationFragment()
                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, fragment)?.commit()
                    }
                    .addOnFailureListener { exception ->
                        // Manejar errores aquí
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
