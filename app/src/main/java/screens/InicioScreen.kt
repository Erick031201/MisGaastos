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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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

    var gastoEditando by remember { mutableStateOf<Gasto?>(null) }

    var categoriaExpandida by remember { mutableStateOf(false) }

    val categorias = listOf(
        "Comida",
        "Transporte",
        "Hogar",
        "Salud",
        "Educación",
        "Entretenimiento",
        "Compras",
        "Otros"
    )

    val total = gastos.sumOf { it.monto }

    // Agrupar gastos por categoría y sumar sus montos
    val resumenCategorias = gastos
        .groupBy { it.categoria }
        .mapValues { (_, gastosCategoria) ->
            gastosCategoria.sumOf { it.monto }
        }
        .toList()
        .sortedByDescending { it.second }

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

            // ENCABEZADO
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

            // TOTAL
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

            // RESUMEN POR CATEGORÍA
            item {
                Text(
                    text = "Resumen por categoría",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (resumenCategorias.isEmpty()) {

                item {
                    Text(
                        text = "Todavía no hay datos para mostrar.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            } else {

                items(
                    items = resumenCategorias,
                    key = { it.first }
                ) { resumen ->

                    val nombreCategoria = resumen.first
                    val totalCategoria = resumen.second

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = nombreCategoria,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "$%.2f".format(totalCategoria),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            // FORMULARIO
            item {
                Spacer(modifier = Modifier.height(8.dp))

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

            // SELECTOR DE CATEGORÍA
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedButton(
                        onClick = {
                            categoriaExpandida = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (categoria.isBlank()) {
                                "Seleccionar categoría"
                            } else {
                                categoria
                            }
                        )
                    }

                    DropdownMenu(
                        expanded = categoriaExpandida,
                        onDismissRequest = {
                            categoriaExpandida = false
                        }
                    ) {

                        categorias.forEach { opcion ->

                            DropdownMenuItem(
                                text = {
                                    Text(opcion)
                                },
                                onClick = {
                                    categoria = opcion
                                    categoriaExpandida = false
                                }
                            )
                        }
                    }
                }
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

            // GUARDAR / ACTUALIZAR
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

                                val gastoActualizado = gastoEditando!!.copy(
                                    descripcion = descripcion,
                                    categoria = categoria,
                                    monto = montoDouble
                                )

                                viewModel.actualizarGasto(gastoActualizado)
                            }

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

            // CANCELAR EDICIÓN
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

            // HISTORIAL
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
                            gastoEditando = gasto
                            descripcion = gasto.descripcion
                            categoria = gasto.categoria
                            monto = gasto.monto.toString()
                        },

                        onEliminar = {
                            viewModel.eliminarGasto(gasto)

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

            OutlinedButton(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar")
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                onClick = onEliminar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar")
            }
        }
    }
}