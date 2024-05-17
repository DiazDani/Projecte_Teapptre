import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jarproductions.projecte_teapptre.R
import com.jarproductions.projecte_teapptre.databinding.FragmentReservationBinding
import com.jarproductions.projecte_teapptre.menu.reservation.ReservationAdapter
import com.jarproductions.projecte_teapptre.reservaTings.Reserva
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReservationFragment : Fragment(), ReservationAdapter.OnReservationItemClickListener {

    private lateinit var binding: FragmentReservationBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationBinding.inflate(inflater, container, false)
        if (binding.root == null) {
            Log.e("ReservationFragment", "Failed to inflate layout")
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadReservas()
    }

    private fun loadReservas() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.email!!)
                .get()
                .addOnSuccessListener { document ->
                    val reservasList = mutableListOf<Reserva>()
                    val reservas = document["reservas"] as? List<Map<String, Any>>
                    reservas?.forEach { reservaMap ->
                        val nombre = reservaMap["reserva"]?.toString() ?: ""
                        val asientos = reservaMap["asiento"]?.toString() ?: ""
                        firestore.collection("obras")
                            .whereEqualTo("nombre", nombre)
                            .get()
                            .addOnSuccessListener { obrasDocuments ->
                                obrasDocuments.forEach { obraDocument ->
                                    val fechaTimestamp = obraDocument["fecha"] as? com.google.firebase.Timestamp
                                    val fecha = formatDate(fechaTimestamp?.toDate())
                                    val portadaUrl = obraDocument.getString("portada") ?: "" // Obtén la URL de la imagen
                                    val reserva = Reserva(nombre, fecha, asientos, portadaUrl)
                                    reservasList.add(reserva)
                                }
                                setupRecyclerView(reservasList)
                            }
                            .addOnFailureListener { exception ->
                                Log.e("ReservationFragment", "Error al cargar la fecha de la obra: ${exception.message}")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ReservationFragment", "Error al cargar las reservas: ${exception.message}")
                }
        }
    }
    private fun formatDate(date: Date?): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault())
        return date?.let { dateFormat.format(it) } ?: ""
    }

    private fun setupRecyclerView(reservas: List<Reserva>) {
        if (reservas.isNotEmpty()) {
            val adapter = ReservationAdapter(reservas, this)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter
        } else {
            Log.e("ReservationFragment", "No hay datos para mostrar")
        }
    }

    override fun onItemClick(reserva: Reserva) {
        // Reemplazar el fragmento actual con ObraDetailsFragment
        val fragment = ObraDetailsFragment.newInstance(
            reserva.nombre, // Utilizamos el nombre de la reserva como referencia para el fragmento
            "" // No estoy seguro de qué debería ir aquí
        )
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null) // Permite volver al fragmento anterior al pulsar hacia atrás
            .commit()
    }






}
