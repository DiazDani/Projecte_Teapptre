package com.jarproductions.projecte_teapptre.obraThings

data class Obra(
    val nombre: String,
    val portada: String?,
    val asientos: Int,
    val descripcion: String,
    val asientosRestantes: Int,
    val fecha: String? = null
)
