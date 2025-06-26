package com.ud.parcial2.network

import com.ud.parcial2.model.Subasta
import com.ud.parcial2.model.PujaRequest
import com.ud.parcial2.model.GanadorResponse
import retrofit2.Response
import retrofit2.http.*

// Interfaz que define los endpoints para consumir la API REST usando Retrofit
interface SubastaApiService {

    // Obtener la lista de todas las subastas disponibles
    @GET("subastas")
    suspend fun getSubastas(): List<Subasta>

    // Obtener el detalle de una subasta específica mediante su ID
    @GET("subastas/{id}")
    suspend fun getDetalleSubasta(@Path("id") id: Int): Subasta

    // Obtener todas las pujas registradas (posiblemente para filtrar por subasta luego)
    @GET("pujas")
    suspend fun obtenerPujas(): List<PujaRequest>

    // Enviar una nueva puja a la API (incluye usuario, monto, posición, etc.)
    @POST("pujas")
    suspend fun enviarPuja(@Body puja: PujaRequest): PujaRequest

    // Obtener la lista de ganadores registrados por el backend
    @GET("ganadores")
    suspend fun obtenerGanadores(): List<GanadorResponse>

    // Crear una nueva subasta enviando los datos como un objeto Subasta
    @POST("subastas")
    suspend fun crearSubasta(@Body subasta: Subasta): Subasta

    @PATCH("subastas/{id}")
    suspend fun finalizarSubasta(@Path("id") id: Int, @Body subasta: Subasta): Subasta

}
