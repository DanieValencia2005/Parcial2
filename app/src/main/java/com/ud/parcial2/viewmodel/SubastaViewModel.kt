package com.ud.parcial2.viewmodel

import androidx.lifecycle.*
import com.ud.parcial2.model.PujaRequest
import com.ud.parcial2.model.Subasta
import com.ud.parcial2.model.GanadorResponse
import kotlinx.coroutines.launch

import com.ud.parcial2.repository.SubastaRepository



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
    fun enviarPuja(subastaId: Int, usuario: String, monto: Double, posicion: Int, imagenUrl: String? = null) = viewModelScope.launch {
        val puja = PujaRequest(subastaId = subastaId, usuario = usuario, monto = monto, posicion = posicion, imagenUrl = imagenUrl)
        repo.enviarPuja(puja)
        cargarPujas()
    }



    fun consultarGanador(id: Int) = viewModelScope.launch {
        val lista = repo.obtenerGanadores()  // ← carga la lista completa
        ganador.value = lista.find { it.id == id }  // ← filtra por ID
    }
    fun crearSubasta(titulo: String, descripcion: String, estado: String) = viewModelScope.launch {
        val nuevaSubasta = Subasta(titulo = titulo, descripcion = descripcion, estado = estado)
        repo.crearSubasta(nuevaSubasta)
        cargarSubastas() // opcional: refresca la lista
    }


}

