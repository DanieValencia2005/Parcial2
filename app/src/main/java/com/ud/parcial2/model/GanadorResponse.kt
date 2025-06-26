package com.ud.parcial2.model

// Data class que representa la respuesta del backend con la información del ganador de una subasta
data class GanadorResponse(
    val id: Int,                  // ID de la puja ganadora
    val subastaId: Int,           // ID de la subasta correspondiente
    val nombre: String,           // Nombre del ganador
    val montoGanador: Double      // Monto ganador
)

