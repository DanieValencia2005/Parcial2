package com.ud.parcial2.model

data class Subasta(
    val id: Int? = null, // ← cambiado
    val titulo: String,
    val descripcion: String,
    val estado: String
)
