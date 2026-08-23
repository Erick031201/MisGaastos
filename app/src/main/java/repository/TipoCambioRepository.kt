package com.example.misgastos.repository

import com.example.misgastos.network.RetrofitClient
import com.example.misgastos.network.TipoCambioResponse

class TipoCambioRepository {

    suspend fun obtenerTipoCambio(): TipoCambioResponse {
        return RetrofitClient.api.obtenerTipoCambio()
    }
}