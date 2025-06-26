package com.ud.parcial2.viewmodel

import androidx.lifecycle.*
import com.ud.parcial2.model.PujaRequest
import com.ud.parcial2.model.Subasta
import com.ud.parcial2.model.GanadorResponse
import kotlinx.coroutines.launch
import com.ud.parcial2.repository.SubastaRepository

// ViewModel que gestiona la lógica y los datos de la app de subastas
class SubastaViewModel(private val repo: SubastaRepository) : ViewModel() {

    // LiveData para observar la lista de subastas disponibles
    val subastas = MutableLiveData<List<Subasta>>()

    // LiveData que contiene el detalle de una subasta seleccionada
    val detalleSubasta = MutableLiveData<Subasta>()

    // LiveData que contiene todas las pujas realizadas
    val pujas = MutableLiveData<List<PujaRequest>>()

    // LiveData que representa al ganador de una subasta (si ya terminó)
    val ganador = MutableLiveData<GanadorResponse>()

    // Carga todas las subastas desde el repositorio
    fun cargarSubastas() = viewModelScope.launch {
        subastas.value = repo.obtenerSubastas()
    }

    // Carga los detalles de una subasta específica
    fun cargarDetalle(id: Int) = viewModelScope.launch {
        detalleSubasta.value = repo.detalleSubasta(id)
    }

    // Carga todas las pujas realizadas
    fun cargarPujas() = viewModelScope.launch {
        pujas.value = repo.obtenerPujas()
    }

    // Envía una nueva puja y actualiza la lista de pujas
    fun enviarPuja(subastaId: Int, usuario: String, monto: Double, posicion: Int? = null, imagenUrl: String? = null) = viewModelScope.launch {
        val puja = PujaRequest(
            id = null,
            subastaId = subastaId,
            usuario = usuario,
            monto = monto,
            posicion = posicion ?: 0, // Por si no estás usando posición aún
            imagenUrl = imagenUrl
        )
        repo.enviarPuja(puja)
        cargarPujas()
    }


    // Consulta al ganador de una subasta finalizada
    fun consultarGanador(subastaId: Int) = viewModelScope.launch {
        val lista = repo.obtenerGanadores()
        ganador.value = lista.find { it.subastaId == subastaId }
    }


    // Crea una nueva subasta y recarga la lista de subastas
    fun crearSubasta(titulo: String, descripcion: String, estado: String, imagenUrl: String?) = viewModelScope.launch {
        val nueva = Subasta(null, titulo, descripcion, estado, imagenUrl)
        repo.crearSubasta(nueva)
        cargarSubastas()
    }
    fun finalizarSubasta(id: Int) = viewModelScope.launch {
        detalleSubasta.value?.let { actual ->

            val finalizada = actual.copy(estado = "finalizada")
            repo.finalizarSubasta(id, finalizada)
            cargarDetalle(id)

            // PASO 1: Cargar pujas si no se han cargado
            if (pujas.value.isNullOrEmpty()) {
                cargarPujas()
            }

            // PASO 2: Encontrar la puja ganadora
            val pujasSubasta = pujas.value?.filter { it.subastaId == id }.orEmpty()
            val mejorPuja = pujasSubasta.maxByOrNull { it.monto }

            // PASO 3: Si hay una puja ganadora, actualizar LiveData
            mejorPuja?.let {
                val ganadorSimulado = GanadorResponse(
                    id = it.id ?: 0,
                    subastaId = it.subastaId,
                    nombre = it.usuario,
                    montoGanador = it.monto
                )
                ganador.value = ganadorSimulado
            }
        }
    }



}
