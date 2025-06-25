package com.ud.parcial2.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ud.parcial2.viewmodel.SubastaViewModel

@Composable
fun CrearSubastaScreen(navController: NavController, viewModel: SubastaViewModel) {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

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

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                viewModel.crearSubasta(titulo, descripcion, "activa")
                Toast.makeText(context, "Subasta creada", Toast.LENGTH_SHORT).show()
                navController.navigate("lista") // ← regresar
            } else {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Crear")
        }
    }
}