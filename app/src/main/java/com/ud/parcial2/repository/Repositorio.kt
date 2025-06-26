package com.ud.parcial2.repository

import com.ud.parcial2.model.GanadorResponse
import com.ud.parcial2.model.PujaRequest
import com.ud.parcial2.model.Subasta // ← ¡IMPORTANTE!
import com.ud.parcial2.network.RetrofitInstance

// Repositorio que centraliza el acceso a la API REST
class SubastaRepository {

    // Instancia del servicio Retrofit que contiene los métodos definidos en la interfaz
    private val api = RetrofitInstance.api

    // Obtiene la lista completa de subastas
    suspend fun obtenerSubastas() = api.getSubastas()

    // Obtiene los detalles de una subasta específica por ID
    suspend fun detalleSubasta(id: Int) = api.getDetalleSubasta(id)

    // Obtiene todas las pujas registradas en el sistema
    suspend fun obtenerPujas() = api.obtenerPujas()

    // Envía una nueva puja para una subasta
    suspend fun enviarPuja(puja: PujaRequest) = api.enviarPuja(puja)

    // Recupera la lista de ganadores registrados
    suspend fun obtenerGanadores() = api.obtenerGanadores()

    // Crea una nueva subasta con los datos proporcionados
    suspend fun crearSubasta(subasta: Subasta) = api.crearSubasta(subasta)

    suspend fun finalizarSubasta(id: Int, subasta: Subasta) = api.finalizarSubasta(id, subasta)

}
