package com.ud.parcial2.viewmodel

import androidx.lifecycle.*
import com.ud.parcial2.model.PujaRequest
import com.ud.parcial2.model.Subasta
import com.ud.parcial2.model.GanadorResponse
import kotlinx.coroutines.launch

import com.ud.parcial2.repository.SubastaRepository



// com/ud/parcial2/viewmodel/SubastaViewModel.kt
class SubastaViewModel(private val repo: SubastaRepository) : ViewModel() {
    val subastas = MutableLiveData<List<Subasta>>()
    val detalleSubasta = MutableLiveData<Subasta>()
    val pujas = MutableLiveData<List<PujaRequest>>()
    val ganador = MutableLiveData<GanadorResponse>()

    fun cargarSubastas() = viewModelScope.launch {
        subastas.value = repo.obtenerSubastas()
    }

    fun cargarDetalle(id: Int) = viewModelScope.launch {
        detalleSubasta.value = repo.detalleSubasta(id)
    }

    fun cargarPujas() = viewModelScope.launch {
        pujas.value = repo.obtenerPujas()
    }

    fun enviarPuja(subastaId: Int, usuario: String, monto: Double, posicion: Int, imagenUrl: String?) = viewModelScope.launch {
        val puja = PujaRequest(null, subastaId, usuario, monto, posicion, imagenUrl)
        repo.enviarPuja(puja)
        cargarPujas()
    }


    fun consultarGanador(id: Int) = viewModelScope.launch {
        val lista = repo.obtenerGanadores()
        ganador.value = lista.find { it.id == id }
    }

    fun crearSubasta(titulo: String, descripcion: String, estado: String, imagenUrl: String?) = viewModelScope.launch {
        val nueva = Subasta(null, titulo, descripcion, estado, imagenUrl)
        repo.crearSubasta(nueva)
        cargarSubastas()
    }
}


