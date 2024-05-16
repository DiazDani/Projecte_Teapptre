package com.jarproductions.projecte_teapptre

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jarproductions.projecte_teapptre.databinding.ObraItemBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date


class ObraViewHolder(private val binding: ObraItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val nombreTextView: TextView = binding.nombreTextView
    private val dateTextView: TextView = binding.dateTextView

    fun bind(obra: Obra) {
        nombreTextView.text = obra.nombre
        dateTextView.text = formatDate(obra.fecha)
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
}

