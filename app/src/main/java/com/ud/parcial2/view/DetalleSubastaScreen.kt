package com.ud.parcial2.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ud.parcial2.viewmodel.SubastaViewModel

@Composable
fun DetalleSubastaScreen(navController: NavController, vm: SubastaViewModel, subastaId: Int) {
    val det by vm.detalleSubasta.observeAsState()
    val pujas by vm.pujas.observeAsState(emptyList())
    val context = LocalContext.current
    val ganador by vm.ganador.observeAsState()

    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var posSel by remember { mutableStateOf<Int?>(null) }
    var pujaUrl by remember { mutableStateOf("") }

    LaunchedEffect(subastaId) {
        vm.cargarDetalle(subastaId)
        vm.cargarPujas()
    }

    det?.let { s ->
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(s.titulo, style = MaterialTheme.typography.headlineSmall)
            Text(s.descripcion, style = MaterialTheme.typography.bodyMedium)

            s.imagenUrl?.let {
                Spacer(Modifier.height(8.dp))
                AsyncImage(model = it, contentDescription = null, modifier = Modifier.size(120.dp))
            }

            Spacer(Modifier.height(20.dp))

            if (s.estado == "activa") {
                val ocup = pujas.filter { it.subastaId == subastaId }.map { it.posicion }

                for (row in 0 until 10) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0 until 10) {
                            val pos = row * 10 + col
                            val ocupado = pos in ocup

                            Button(
                                onClick = {
                                    if (!ocupado) posSel = pos
                                },
                                enabled = true,
                                modifier = Modifier.size(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        ocupado -> Color(0xFFD32F2F) // Rojo fuerte
                                        pos == posSel -> Color.Blue
                                        else -> Color.LightGray
                                    },
                                    contentColor = Color.White
                                )
                            ) {
                                Text(pos.toString(), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Tu nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pujaUrl,
                    onValueChange = { pujaUrl = it },
                    label = { Text("URL imagen puja (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    if (nombre.isBlank() || monto.toDoubleOrNull() == null || posSel == null) {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        vm.enviarPuja(
                            subastaId = subastaId,
                            usuario = nombre,
                            monto = monto.toDouble(),
                            posicion = posSel!!,
                            imagenUrl = pujaUrl.ifBlank { null }
                        )
                        Toast.makeText(context, "Puja enviada", Toast.LENGTH_SHORT).show()
                        posSel = null
                        nombre = ""
                        monto = ""
                        pujaUrl = ""
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Enviar puja")
                }
            } else {
                LaunchedEffect(subastaId) {
                    vm.consultarGanador(subastaId)
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
