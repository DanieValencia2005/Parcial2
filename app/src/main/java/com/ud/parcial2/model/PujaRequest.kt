package com.ud.parcial2.model

// PujaRequest.kt
data class PujaRequest(
    val id: Int? = null,
    val subastaId: Int,
    val usuario: String,
    val monto: Double,
    val posicion: Int,
    val imagenUrl: String? = null // ← NUEVO
)



