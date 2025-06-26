package com.ud.parcial2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ud.parcial2.viewmodel.SubastaViewModel

@Composable
fun ListaSubastasScreen(navController: NavController, viewModel: SubastaViewModel) {
    val subastas by viewModel.subastas.observeAsState(emptyList())
    var busqueda by remember { mutableStateOf("") } // Texto de búsqueda

    // Cargar las subastas al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarSubastas()
    }

    // Filtra las subastas según el texto de búsqueda
    val subastasFiltradas = subastas.filter {
        it.titulo.contains(busqueda, ignoreCase = true)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Campo de texto para buscar subastas por nombre
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar subasta por nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de subastas filtradas
        LazyColumn {
            items(subastasFiltradas) { subasta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("detalle/${subasta.id}")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = subasta.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(text = subasta.descripcion, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
