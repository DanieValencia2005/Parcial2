package com.ud.parcial2.model

// Data class que representa una Subasta en la aplicación
data class Subasta(
    val id: Int? = null,           // ID único de la subasta (puede ser nulo si aún no ha sido asignado por el backend)
    val titulo: String,            // Título de la subasta (por ejemplo, el nombre del producto o ítem)
    val descripcion: String,       // Descripción detallada de la subasta (información del producto, condiciones, etc.)
    val estado: String,            // Estado actual de la subasta (por ejemplo: "abierta", "cerrada", "finalizada")
    val imagenUrl: String? = null  // URL de la imagen asociada a la subasta (opcional, puede ser nula si no hay imagen)
)
