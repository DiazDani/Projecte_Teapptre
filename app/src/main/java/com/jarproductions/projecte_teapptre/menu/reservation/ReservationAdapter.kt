package com.jarproductions.projecte_teapptre.menu.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

            // Cargar la imagen con Glide sin placeholder
            Glide.with(binding.imageView2.context)
                .load(reserva.portadaUrl)
                .into(binding.imageView2)
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
        val binding = ReservationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        holder.bind(reservas[position])
    }

    override fun getItemCount(): Int = reservas.size
}
