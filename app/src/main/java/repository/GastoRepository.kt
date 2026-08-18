package com.example.misgastos.repository

import com.example.misgastos.data.local.Gasto
import com.example.misgastos.data.local.GastoDao
import kotlinx.coroutines.flow.Flow

class GastoRepository(
    private val gastoDao: GastoDao
) {

    fun obtenerTodos(): Flow<List<Gasto>> {
        return gastoDao.obtenerTodos()
    }

    suspend fun insertar(gasto: Gasto) {
        gastoDao.insertar(gasto)
    }

    suspend fun eliminar(gasto: Gasto) {
        gastoDao.eliminar(gasto)
    }
}