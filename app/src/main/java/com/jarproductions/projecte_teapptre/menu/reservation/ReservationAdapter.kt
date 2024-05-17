package com.jarproductions.projecte_teapptre.menu.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarproductions.projecte_teapptre.databinding.ReservationItemBinding
import com.jarproductions.projecte_teapptre.reservaTings.Reserva

class ReservationAdapter(private val reservas: List<Reserva>, private val listener: OnReservationItemClickListener) :
    RecyclerView.Adapter<ReservationAdapter.ReservaViewHolder>() {

    interface OnReservationItemClickListener {
        fun onItemClick(reserva: Reserva)
    }

    inner class ReservaViewHolder(private val binding: ReservationItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(reserva: Reserva) {
            binding.nombreTextView.text = reserva.nombre
            binding.dateTextView.text = reserva.fecha
            binding.seatTextView.text = "Seat: ${reserva.asientos}"
        }


        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val reserva = reservas[position]
                listener.onItemClick(reserva)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val binding =
            ReservationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.bind(reserva)
    }

    override fun getItemCount(): Int {
        return reservas.size
    }
}
