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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.misgastos.viewmodel.GastoViewModel
import com.example.misgastos.viewmodel.TipoCambioUiState
import com.example.misgastos.viewmodel.TipoCambioViewModel

@Composable
fun InicioScreen(
    viewModel: GastoViewModel
) {

    val gastos by viewModel.gastos.collectAsState()

    val tipoCambioViewModel: TipoCambioViewModel =
        viewModel()

    val estadoTipoCambio by
    tipoCambioViewModel.estado.collectAsState()

    val total =
        gastos.sumOf { it.monto }

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

    val resumenCategorias =
        categorias.mapNotNull { categoriaActual ->

            val gastosCategoria =
                gastos.filter {

                    it.categoria
                        .trim()
                        .equals(
                            categoriaActual,
                            ignoreCase = true
                        )
                }

            if (gastosCategoria.isNotEmpty()) {

                categoriaActual to
                        gastosCategoria.sumOf {
                            it.monto
                        }

            } else {

                null
            }
        }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        item {

            Text(
                text = "Mis Gastos",
                style =
                    MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "Resumen general de tus gastos personales.",
                style =
                    MaterialTheme.typography.bodyMedium
            )
        }

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme
                            .colorScheme
                            .primaryContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Total gastado",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Text(
                        text =
                            "$%.2f".format(total),
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall
                    )

                    Text(
                        text =
                            "Gastos registrados: ${gastos.size}"
                    )
                }
            }
        }

        item {

            Text(
                text = "Tipo de cambio",
                style =
                    MaterialTheme.typography.titleLarge
            )
        }

        item {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    when (
                        val estado = estadoTipoCambio
                    ) {

                        is TipoCambioUiState.Cargando -> {

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically,
                                horizontalArrangement =
                                    Arrangement.spacedBy(12.dp)
                            ) {

                                CircularProgressIndicator()

                                Text(
                                    text =
                                        "Cargando tipo de cambio..."
                                )
                            }
                        }

                        is TipoCambioUiState.Exito -> {

                            Text(
                                text = "USD → EUR",
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium
                            )

                            Text(
                                text =
                                    "1 USD = %.4f EUR"
                                        .format(estado.tasa),
                                style =
                                    MaterialTheme
                                        .typography
                                        .headlineSmall
                            )

                            Text(
                                text =
                                    "Fecha: ${estado.fecha}"
                            )

                            if (total > 0) {

                                Text(
                                    text =
                                        "Tus $%.2f equivalen aproximadamente a €%.2f"
                                            .format(
                                                total,
                                                total * estado.tasa
                                            )
                                )
                            }
                        }

                        is TipoCambioUiState.Error -> {

                            Text(
                                text = estado.mensaje,
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .error
                            )

                            Button(
                                onClick = {
                                    tipoCambioViewModel
                                        .obtenerTipoCambio()
                                }
                            ) {

                                Text("REINTENTAR")
                            }
                        }
                    }
                }
            }
        }

        item {

            Text(
                text = "Resumen por categoría",
                style =
                    MaterialTheme.typography.titleLarge
            )
        }

        if (resumenCategorias.isEmpty()) {

            item {

                Text(
                    text =
                        "Todavía no hay gastos registrados."
                )
            }

        } else {

            items(
                items = resumenCategorias,
                key = {
                    it.first
                }
            ) { resumen ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        Text(
                            text = resumen.first,
                            modifier =
                                Modifier.weight(1f),
                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium
                        )

                        Text(
                            text =
                                "$%.2f"
                                    .format(
                                        resumen.second
                                    ),
                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium
                        )
                    }
                }
            }
        }
    }
}