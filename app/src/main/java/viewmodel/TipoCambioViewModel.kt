package com.example.misgastos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misgastos.repository.TipoCambioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TipoCambioUiState {

    object Cargando : TipoCambioUiState()

    data class Exito(
        val tasa: Double,
        val fecha: String
    ) : TipoCambioUiState()

    data class Error(
        val mensaje: String
    ) : TipoCambioUiState()
}

class TipoCambioViewModel : ViewModel() {

    private val repository =
        TipoCambioRepository()

    private val _estado =
        MutableStateFlow<TipoCambioUiState>(
            TipoCambioUiState.Cargando
        )

    val estado: StateFlow<TipoCambioUiState> =
        _estado.asStateFlow()

    init {
        obtenerTipoCambio()
    }

    fun obtenerTipoCambio() {

        viewModelScope.launch {

            _estado.value =
                TipoCambioUiState.Cargando

            try {

                val respuesta =
                    repository.obtenerTipoCambio()

                _estado.value =
                    TipoCambioUiState.Exito(
                        tasa = respuesta.rate,
                        fecha = respuesta.date
                    )

            } catch (e: Exception) {

                _estado.value =
                    TipoCambioUiState.Error(
                        mensaje =
                            "No se pudo obtener el tipo de cambio."
                    )
            }
        }
    }
}