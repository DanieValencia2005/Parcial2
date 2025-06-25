package com.ud.parcial2.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ud.parcial2.viewmodel.SubastaViewModel

@Composable
fun DetalleSubastaScreen(
    navController: NavController,
    viewModel: SubastaViewModel,
    subastaId: Int
) {
    val detalle by viewModel.detalleSubasta.observeAsState()
    val pujas by viewModel.pujas.observeAsState(emptyList())
    val ganador by viewModel.ganador.observeAsState()
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var posicionSeleccionada by remember { mutableStateOf<Int?>(null) }
    var imagenUrl by remember { mutableStateOf("") }

    LaunchedEffect(subastaId) {
        viewModel.cargarDetalle(subastaId)
        viewModel.cargarPujas()
    }

    detalle?.let { subasta ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(subasta.titulo, style = MaterialTheme.typography.headlineSmall)
            Text(subasta.descripcion, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(20.dp))

            if (subasta.estado == "activa") {
                val posicionesOcupadas = pujas.filter { it.subastaId == subastaId }.map { it.posicion }

                Column {
                    for (row in 0 until 10) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (col in 0 until 10) {
                                val pos = row * 10 + col
                                val ocupado = pos in posicionesOcupadas
                                Button(
                                    onClick = { if (!ocupado) posicionSeleccionada = pos },
                                    enabled = !ocupado,
                                    modifier = Modifier.size(32.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = when {
                                            ocupado -> MaterialTheme.colorScheme.error
                                            pos == posicionSeleccionada -> MaterialTheme.colorScheme.primary
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    )
                                ) {
                                    Text(pos.toString(), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    nombre,
                    { nombre = it },
                    label = { Text("Tu nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    monto,
                    { monto = it },
                    label = { Text("Monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    imagenUrl,
                    { imagenUrl = it },
                    label = { Text("URL de imagen (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (imagenUrl.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    AsyncImage(
                        model = imagenUrl,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (nombre.isBlank() || monto.toDoubleOrNull() == null || posicionSeleccionada == null) {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.enviarPuja(
                                subastaId,
                                nombre,
                                monto.toDouble(),
                                posicionSeleccionada!!,
                                imagenUrl
                            )
                            Toast.makeText(context, "Puja enviada", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enviar puja")
                }
            } else {
                LaunchedEffect(subastaId) {
                    viewModel.consultarGanador(subastaId)
                }
                ganador?.let {
                    Spacer(Modifier.height(20.dp))
                    Text("Ganador: ${it.nombre}", style = MaterialTheme.typography.bodyLarge)
                    Text("Monto: ${it.montoGanador}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
