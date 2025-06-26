package com.ud.parcial2.model

// Data class que representa los datos enviados al backend al realizar una puja en una subasta
data class PujaRequest(
    val id: Int? = null,               // ID único de la puja (puede ser generado automáticamente por el backend)
    val subastaId: Int,                // ID de la subasta a la que pertenece la puja
    val usuario: String,               // Nombre del usuario que realiza la puja
    val monto: Double,                 // Monto ofrecido en la puja
    val posicion: Int,                 // Posición en la matriz (del 0 al 99) que el usuario desea ocupar
    val imagenUrl: String? = null      // URL de la imagen asociada al usuario o la puja (opcional)
)
