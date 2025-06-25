package com.ud.parcial2.view
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
    val navController = rememberNavController()
    val viewModel: SubastaViewModel = viewModel(factory = SubastaViewModelFactory())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("crear") }) {
                Icon(Icons.Default.Add, contentDescription = "Crear")
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "lista",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("lista") {
                ListaSubastasScreen(navController, viewModel)
            }
            composable("crear") {
                CrearSubastaScreen(navController, viewModel)
            }
            composable(
                route = "detalle/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                DetalleSubastaScreen(navController, viewModel, id)
            }
        }
    }
}
