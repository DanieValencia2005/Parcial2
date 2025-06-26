package com.ud.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ud.parcial2.repository.SubastaRepository

// Fábrica personalizada para crear instancias del ViewModel SubastaViewModel
class SubastaViewModelFactory : ViewModelProvider.Factory {

    // Método que se encarga de crear una instancia del ViewModel solicitado
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Se crea una instancia del repositorio que usará el ViewModel
        val repo = SubastaRepository()

        // Se verifica si el ViewModel solicitado es del tipo SubastaViewModel
        if (modelClass.isAssignableFrom(SubastaViewModel::class.java)) {
            // Se retorna una nueva instancia del ViewModel con su repositorio
            return SubastaViewModel(repo) as T
        }

        // Si no es el tipo esperado, se lanza una excepción
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
