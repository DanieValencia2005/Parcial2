package com.ud.parcial2.network

import com.ud.parcial2.model.Subasta
import com.ud.parcial2.model.PujaRequest
import com.ud.parcial2.model.GanadorResponse
import retrofit2.Response
import retrofit2.http.*

// com/ud/parcial2/network/SubastaApiService.kt
interface SubastaApiService {
    @GET("subastas") suspend fun getSubastas(): List<Subasta>
    @GET("subastas/{id}") suspend fun getDetalleSubasta(@Path("id") id: Int): Subasta
    @GET("pujas") suspend fun obtenerPujas(): List<PujaRequest>
    @POST("pujas") suspend fun enviarPuja(@Body puja: PujaRequest): PujaRequest
    @GET("ganadores") suspend fun obtenerGanadores(): List<GanadorResponse>
    @POST("subastas") suspend fun crearSubasta(@Body subasta: Subasta): Subasta
}

