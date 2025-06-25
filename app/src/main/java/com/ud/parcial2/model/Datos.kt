package com.ud.parcial2.model


data class Subasta(
    val id: Int? = null,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val imagenUrl: String? = null // ← AGREGADO
)

