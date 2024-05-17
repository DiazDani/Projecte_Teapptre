package com.jarproductions.projecte_teapptre.reservaTings

data class Reserva(
    val nombre: String,
    val fecha: String,
    val asientos: String,
    val portadaUrl: String // Añadir este campo para la URL de la imagen
)
