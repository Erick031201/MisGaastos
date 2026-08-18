package com.example.misgastos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misgastos.data.local.Gasto
import com.example.misgastos.repository.GastoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GastoViewModel(
    private val repository: GastoRepository
) : ViewModel() {

    val gastos: StateFlow<List<Gasto>> =
        repository.obtenerTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun insertarGasto(gasto: Gasto) {
        viewModelScope.launch {
            repository.insertar(gasto)
        }
    }

    fun eliminarGasto(gasto: Gasto) {
        viewModelScope.launch {
            repository.eliminar(gasto)
        }
    }
}