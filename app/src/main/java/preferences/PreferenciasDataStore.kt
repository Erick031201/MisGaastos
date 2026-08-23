package com.example.misgastos.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "preferencias"
)

class PreferenciasDataStore(
    private val context: Context
) {

    companion object {
        private val MODO_OSCURO = booleanPreferencesKey(
            "modo_oscuro"
        )
    }

    val modoOscuro: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->

            preferencias[MODO_OSCURO] ?: false
        }

    suspend fun guardarModoOscuro(
        activado: Boolean
    ) {

        context.dataStore.edit { preferencias ->

            preferencias[MODO_OSCURO] = activado
        }
    }
}

