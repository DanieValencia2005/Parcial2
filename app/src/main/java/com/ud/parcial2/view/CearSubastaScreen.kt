package com.ud.parcial2.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ud.parcial2.viewmodel.SubastaViewModel

@Composable
fun CrearSubastaScreen(navController: NavController, vm: SubastaViewModel) {
    val ctx = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Crear nueva subasta", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imagenUrl,
            onValueChange = { imagenUrl = it },
            label = { Text("URL imagen (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (imagenUrl.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = imagenUrl,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (titulo.isBlank() || descripcion.isBlank()) {
                Toast.makeText(ctx, "Completa los campos", Toast.LENGTH_SHORT).show()
            } else {
                vm.crearSubasta(
                    titulo = titulo,
                    descripcion = descripcion,
                    estado = "activa",
                    imagenUrl = imagenUrl.ifBlank { null }
                )
                navController.navigate("lista")
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Crear")
        }
    }
}
