package com.jarproductions.projecte_teapptre.obraThings

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jarproductions.projecte_teapptre.databinding.ObraItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class ObraViewHolder(private val binding: ObraItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val nombreTextView: TextView = binding.nombreTextView
    private val dateTextView: TextView = binding.dateTextView
    private val imageView = binding.imageView2

    fun bind(obra: Obra) {
        nombreTextView.text = obra.nombre
        dateTextView.text = obra.fecha?.let { formatDate(it) } ?: "Fecha no disponible"

        // Obtener la URL de la imagen de la portada de la obra desde el objeto Obra
        val portadaUrl = obra.portada

        // Cargar la imagen de la portada de la obra utilizando Glide
        portadaUrl?.let { url ->
            Glide.with(itemView.context)
                .load(url)
                .into(imageView)
        }
    }



    fun formatDate(date: String?): String {
        if (date.isNullOrEmpty()) {
            return "Fecha no disponible"
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val parsedDate = sdf.parse(date)
            SimpleDateFormat("dd MMMM , yyyy", Locale.getDefault()).format(parsedDate)
        } catch (e: ParseException) {
            "Fecha no disponible"
        }
    }

}

