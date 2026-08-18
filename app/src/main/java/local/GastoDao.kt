package com.example.misgastos.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    @Insert
    suspend fun insertar(gasto: Gasto)

    @Update
    suspend fun actualizar(gasto: Gasto)

    @Delete
    suspend fun eliminar(gasto: Gasto)

    @Query("SELECT * FROM gastos ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Gasto>>
}