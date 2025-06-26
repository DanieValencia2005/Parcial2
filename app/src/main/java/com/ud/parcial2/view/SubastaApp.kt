package com.ud.parcial2.view

// Importaciones necesarias para navegación, vista y ViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ud.parcial2.viewmodel.SubastaViewModel
import com.ud.parcial2.viewmodel.SubastaViewModelFactory

@Composable
fun SubastaApp() {
    // Controlador de navegación entre pantallas
    val nav = rememberNavController()

    // Obtiene una instancia del ViewModel con su fábrica personalizada
    val vm: SubastaViewModel = viewModel(factory = SubastaViewModelFactory())

    // Contenedor principal de la UI, incluye botón flotante y navegación interna
    Scaffold(
        floatingActionButton = {
            // Botón flotante para crear una nueva subasta
            FloatingActionButton(onClick = { nav.navigate("crear") }) {
                Icon(Icons.Default.Add, contentDescription = "Crear")
            }
        }
    ) { pad -> // Padding proporcionado por Scaffold
        // Sistema de navegación entre pantallas
        NavHost(nav, startDestination = "lista", Modifier.padding(pad)) {
            // Pantalla principal: lista de subastas
            composable("lista") {
                ListaSubastasScreen(nav, vm)
            }
            // Pantalla para crear una nueva subasta
            composable("crear") {
                CrearSubastaScreen(nav, vm)
            }
            // Pantalla de detalle de una subasta con argumento de ID
            composable(
                "detalle/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val id = it.arguments!!.getInt("id")
                DetalleSubastaScreen(nav, vm, id)
            }
        }
    }
}
