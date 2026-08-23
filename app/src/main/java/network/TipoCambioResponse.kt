package com.example.misgastos.network

data class TipoCambioResponse(
    val date: String,
    val base: String,
    val quote: String,
    val rate: Double
)

