package com.jarproductions.projecte_teapptre.obraThings

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarproductions.projecte_teapptre.databinding.ObraItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class ObraViewHolder(private val binding: ObraItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val nombreTextView: TextView = binding.nombreTextView
    private val dateTextView: TextView = binding.dateTextView

    fun bind(obra: Obra) {
        nombreTextView.text = obra.nombre
        dateTextView.text = obra.fecha?.let { formatDate(it) } ?: "Fecha no disponible"
    }


    fun formatDate(date: String?): String {
        if (date.isNullOrEmpty()) {
            return "Fecha no disponible"
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val parsedDate = sdf.parse(date)
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(parsedDate)
        } catch (e: ParseException) {
            "Fecha no disponible"
        }
    }

}

