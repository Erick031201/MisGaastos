package com.example.misgastos.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun ConfiguracionScreen(
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit
) {

    val context = LocalContext.current

    var ubicacion by remember {
        mutableStateOf<Location?>(null)
    }

    var mensajeUbicacion by remember {
        mutableStateOf("Todavía no se ha obtenido la ubicación.")
    }

    fun obtenerUbicacion() {

        val permisoFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val permisoCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (
            permisoFine == PackageManager.PERMISSION_GRANTED ||
            permisoCoarse == PackageManager.PERMISSION_GRANTED
        ) {

            try {

                val locationManager =
                    context.getSystemService(
                        Context.LOCATION_SERVICE
                    ) as LocationManager

                val proveedores =
                    locationManager.getProviders(true)

                var mejorUbicacion: Location? = null

                for (proveedor in proveedores) {

                    val location =
                        locationManager.getLastKnownLocation(
                            proveedor
                        )

                    if (
                        location != null &&
                        (
                                mejorUbicacion == null ||
                                        location.accuracy <
                                        mejorUbicacion.accuracy
                                )
                    ) {

                        mejorUbicacion = location
                    }
                }

                if (mejorUbicacion != null) {

                    ubicacion = mejorUbicacion

                    mensajeUbicacion =
                        "Ubicación obtenida correctamente."

                } else {

                    mensajeUbicacion =
                        "No hay una ubicación disponible. Activa la ubicación e inténtalo nuevamente."
                }

            } catch (e: SecurityException) {

                mensajeUbicacion =
                    "No fue posible acceder a la ubicación."
            }
        }
    }

    val permisoLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->

            val permisoConcedido =
                permisos[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||
                        permisos[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (permisoConcedido) {

                mensajeUbicacion =
                    "Permiso concedido. Obteniendo ubicación..."

                obtenerUbicacion()

            } else {

                mensajeUbicacion =
                    "Permiso de ubicación rechazado."
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Configuración",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Preferencias de MisGastos",
            style =
                MaterialTheme.typography.bodyMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Modo oscuro",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Text(
                        text =
                            "Guardar preferencia con DataStore"
                    )
                }

                Switch(
                    checked = modoOscuro,

                    onCheckedChange = {
                        onModoOscuroChange(it)
                    }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Ubicación GPS",
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Text(
                    text = mensajeUbicacion
                )

                if (ubicacion != null) {

                    Text(
                        text =
                            "Latitud: %.6f"
                                .format(
                                    ubicacion!!.latitude
                                )
                    )

                    Text(
                        text =
                            "Longitud: %.6f"
                                .format(
                                    ubicacion!!.longitude
                                )
                    )
                }

                Button(
                    onClick = {

                        val permisoFine =
                            ContextCompat
                                .checkSelfPermission(
                                    context,
                                    Manifest.permission
                                        .ACCESS_FINE_LOCATION
                                )

                        val permisoCoarse =
                            ContextCompat
                                .checkSelfPermission(
                                    context,
                                    Manifest.permission
                                        .ACCESS_COARSE_LOCATION
                                )

                        if (
                            permisoFine ==
                            PackageManager
                                .PERMISSION_GRANTED ||
                            permisoCoarse ==
                            PackageManager
                                .PERMISSION_GRANTED
                        ) {

                            mensajeUbicacion =
                                "Obteniendo ubicación..."

                            obtenerUbicacion()

                        } else {

                            permisoLauncher.launch(
                                arrayOf(
                                    Manifest.permission
                                        .ACCESS_FINE_LOCATION,

                                    Manifest.permission
                                        .ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("OBTENER UBICACIÓN")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Funciones implementadas",
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Text(
                    text = "• Preferencias con DataStore"
                )

                Text(
                    text = "• Modo oscuro persistente"
                )

                Text(
                    text = "• GPS con permiso en tiempo de ejecución"
                )
            }
        }
    }
}