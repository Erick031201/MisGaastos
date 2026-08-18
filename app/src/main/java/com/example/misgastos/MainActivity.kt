package com.example.misgastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.misgastos.data.local.MisGastosDatabase
import com.example.misgastos.repository.GastoRepository
import com.example.misgastos.screens.InicioScreen
import com.example.misgastos.ui.theme.MisGastosTheme
import com.example.misgastos.viewmodel.GastoViewModel
import com.example.misgastos.viewmodel.GastoViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = MisGastosDatabase.getDatabase(applicationContext)
        val repository = GastoRepository(database.gastoDao())

        setContent {
            MisGastosTheme {

                val viewModel: GastoViewModel = viewModel(
                    factory = GastoViewModelFactory(repository)
                )

                InicioScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}