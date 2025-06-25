package com.ud.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ud.parcial2.repository.SubastaRepository

class SubastaViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = SubastaRepository()
        if (modelClass.isAssignableFrom(SubastaViewModel::class.java)) {
            return SubastaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
