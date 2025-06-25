package com.ud.parcial2.repository

import com.ud.parcial2.model.GanadorResponse
import com.ud.parcial2.model.PujaRequest
import com.ud.parcial2.model.Subasta // ← ¡IMPORTANTE!
import com.ud.parcial2.network.RetrofitInstance

class SubastaRepository {
    private val api = RetrofitInstance.api

    suspend fun obtenerSubastas() = api.getSubastas()
    suspend fun detalleSubasta(id: Int) = api.getDetalleSubasta(id)
    suspend fun obtenerPujas() = api.obtenerPujas()
    suspend fun enviarPuja(puja: PujaRequest) = api.enviarPuja(puja)
    suspend fun obtenerGanadores(): List<GanadorResponse> = api.obtenerGanadores()
    suspend fun crearSubasta(subasta: Subasta) = api.crearSubasta(subasta)
}
