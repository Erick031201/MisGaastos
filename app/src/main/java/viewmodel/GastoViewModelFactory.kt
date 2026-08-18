package com.example.misgastos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.misgastos.repository.GastoRepository

class GastoViewModelFactory(
    private val repository: GastoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastoViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}