package com.example.misgastos.network

import retrofit2.http.GET

interface TipoCambioApi {

    @GET("v2/rate/USD/EUR")
    suspend fun obtenerTipoCambio(): TipoCambioResponse
}

