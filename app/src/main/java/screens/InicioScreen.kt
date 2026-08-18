package com.example.misgastos.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.misgastos.data.local.Gasto
import com.example.misgastos.viewmodel.GastoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InicioScreen(
    viewModel: GastoViewModel
) {
    val gastos by viewModel.gastos.collectAsState()

    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }

    // Guarda el gasto que estamos editando.
    var gastoEditando by remember { mutableStateOf<Gasto?>(null) }

    val total = gastos.sumOf { it.monto }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "Mis Gastos",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Controla y registra tus gastos personales.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Total gastado",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "$%.2f".format(total),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = "Gastos registrados: ${gastos.size}"
                        )
                    }
                }
            }

            item {
                Text(
                    text = if (gastoEditando == null) {
                        "Registrar gasto"
                    } else {
                        "Editar gasto"
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = {
                        Text("Descripción")
                    },
                    placeholder = {
                        Text("Ej. Almuerzo")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = {
                        Text("Categoría")
                    },
                    placeholder = {
                        Text("Ej. Comida")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = {
                        Text("Monto")
                    },
                    placeholder = {
                        Text("Ej. 5.50")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }

            item {
                Button(
                    onClick = {

                        val montoDouble = monto.toDoubleOrNull()

                        if (
                            descripcion.isNotBlank() &&
                            categoria.isNotBlank() &&
                            montoDouble != null &&
                            montoDouble > 0
                        ) {

                            if (gastoEditando == null) {

                                // Crear nuevo gasto
                                val fechaActual = SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(Date())

                                val nuevoGasto = Gasto(
                                    descripcion = descripcion,
                                    categoria = categoria,
                                    monto = montoDouble,
                                    fecha = fechaActual
                                )

                                viewModel.insertarGasto(nuevoGasto)

                            } else {

                                // Actualizar gasto existente
                                val gastoActualizado = gastoEditando!!.copy(
                                    descripcion = descripcion,
                                    categoria = categoria,
                                    monto = montoDouble
                                )

                                viewModel.actualizarGasto(gastoActualizado)
                            }

                            // Limpiar formulario
                            descripcion = ""
                            categoria = ""
                            monto = ""
                            gastoEditando = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (gastoEditando == null) {
                            "GUARDAR GASTO"
                        } else {
                            "ACTUALIZAR GASTO"
                        }
                    )
                }
            }

            // Botón para cancelar una edición
            if (gastoEditando != null) {

                item {
                    OutlinedButton(
                        onClick = {
                            descripcion = ""
                            categoria = ""
                            monto = ""
                            gastoEditando = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("CANCELAR EDICIÓN")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Historial de gastos",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (gastos.isEmpty()) {

                item {
                    Text(
                        text = "Todavía no tienes gastos registrados.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            } else {

                items(
                    items = gastos,
                    key = { it.id }
                ) { gasto ->

                    GastoItem(
                        gasto = gasto,

                        onEditar = {

                            // Cargar datos del gasto seleccionado
                            gastoEditando = gasto
                            descripcion = gasto.descripcion
                            categoria = gasto.categoria
                            monto = gasto.monto.toString()
                        },

                        onEliminar = {
                            viewModel.eliminarGasto(gasto)

                            // Si estábamos editando este gasto,
                            // cancelamos la edición.
                            if (gastoEditando?.id == gasto.id) {
                                descripcion = ""
                                categoria = ""
                                monto = ""
                                gastoEditando = null
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: Gasto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = gasto.descripcion,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = gasto.categoria,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = gasto.fecha,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$%.2f".format(gasto.monto),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Editar
            OutlinedButton(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar")
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Botón Eliminar
            OutlinedButton(
                onClick = onEliminar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar")
            }
        }
    }
}