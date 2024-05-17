import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarproductions.projecte_teapptre.databinding.ReservationItemBinding
import com.jarproductions.projecte_teapptre.reservaTings.Reserva
import java.text.SimpleDateFormat
import java.util.Locale

class ReservationAdapter(private val reservas: List<Reserva>) :
    RecyclerView.Adapter<ReservationAdapter.ReservaViewHolder>() {

    inner class ReservaViewHolder(private val binding: ReservationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reserva: Reserva) {
            binding.apply {
                nombreTextView.text = reserva.nombre // Aquí se muestra el nombre de la reserva
                dateTextView.text = reserva.fecha // Aquí se muestra la fecha de la reserva
                seatTextView.text = reserva.asientos // Aquí se muestra el número de asientos reservados
            }
        }

        private fun formatDate(dateString: String): String {
            if (dateString.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val date = dateFormat.parse(dateString)
                return dateFormat.format(date)
            } else {
                return "Fecha no disponible"
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val binding =
            ReservationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        holder.bind(reservas[position])
    }

    override fun getItemCount(): Int {
        return reservas.size
    }
}
