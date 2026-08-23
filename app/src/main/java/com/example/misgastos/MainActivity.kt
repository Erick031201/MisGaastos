package com.example.misgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.misgastos.data.local.MisGastosDatabase
import com.example.misgastos.data.preferences.PreferenciasDataStore
import com.example.misgastos.repository.GastoRepository
import com.example.misgastos.screens.ConfiguracionScreen
import com.example.misgastos.screens.GastosScreen
import com.example.misgastos.screens.InicioScreen
import com.example.misgastos.ui.theme.MisGastosTheme
import com.example.misgastos.viewmodel.GastoViewModel
import com.example.misgastos.viewmodel.GastoViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database =
            MisGastosDatabase.getDatabase(applicationContext)

        val repository =
            GastoRepository(database.gastoDao())

        val preferenciasDataStore =
            PreferenciasDataStore(applicationContext)

        setContent {

            val modoOscuro by
            preferenciasDataStore
                .modoOscuro
                .collectAsState(initial = false)

            val scope = rememberCoroutineScope()

            MisGastosTheme(
                darkTheme = modoOscuro
            ) {

                val gastoViewModel: GastoViewModel =
                    viewModel(
                        factory = GastoViewModelFactory(
                            repository
                        )
                    )

                val navController =
                    rememberNavController()

                val backStackEntry by
                navController
                    .currentBackStackEntryAsState()

                val rutaActual =
                    backStackEntry
                        ?.destination
                        ?.route

                Scaffold(
                    bottomBar = {

                        NavigationBar {

                            NavigationBarItem(
                                selected =
                                    rutaActual == "inicio",

                                onClick = {

                                    navController.navigate(
                                        "inicio"
                                    ) {

                                        popUpTo("inicio") {
                                            inclusive = false
                                        }

                                        launchSingleTop = true
                                    }
                                },

                                icon = {
                                    Text("🏠")
                                },

                                label = {
                                    Text("Inicio")
                                }
                            )

                            NavigationBarItem(
                                selected =
                                    rutaActual == "gastos",

                                onClick = {

                                    navController.navigate(
                                        "gastos"
                                    ) {

                                        launchSingleTop = true
                                    }
                                },

                                icon = {
                                    Text("💰")
                                },

                                label = {
                                    Text("Gastos")
                                }
                            )

                            NavigationBarItem(
                                selected =
                                    rutaActual == "configuracion",

                                onClick = {

                                    navController.navigate(
                                        "configuracion"
                                    ) {

                                        launchSingleTop = true
                                    }
                                },

                                icon = {
                                    Text("⚙️")
                                },

                                label = {
                                    Text("Config.")
                                }
                            )
                        }
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "inicio",
                        modifier =
                            Modifier.padding(innerPadding)
                    ) {

                        composable("inicio") {

                            InicioScreen(
                                viewModel = gastoViewModel
                            )
                        }

                        composable("gastos") {

                            GastosScreen(
                                viewModel = gastoViewModel
                            )
                        }

                        composable("configuracion") {

                            ConfiguracionScreen(
                                modoOscuro = modoOscuro,

                                onModoOscuroChange = {
                                        activado ->

                                    scope.launch {

                                        preferenciasDataStore
                                            .guardarModoOscuro(
                                                activado
                                            )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}