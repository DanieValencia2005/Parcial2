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
    val ganador by vm.ganador.observeAsState()
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var posSel by remember { mutableStateOf<Int?>(null) }

    // Carga los datos iniciales
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
                val ocupadas = pujas.filter { it.subastaId == subastaId }.map { it.posicion }
                // Matriz de posiciones (como ya tienes)
                for (row in 0 until 10) {
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                        for (col in 0 until 10) {
                            val pos = row * 10 + col
                            val ocupado = pos in ocupadas
                            Button(
                                onClick = { if (!ocupado) posSel = pos },
                                modifier = Modifier.size(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        ocupado -> Color.Red
                                        pos == posSel -> Color.Blue
                                        else -> Color.LightGray
                                    }
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
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    val montoD = monto.toDoubleOrNull()
                    if (nombre.isBlank() || montoD == null || posSel == null) {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        vm.enviarPuja(subastaId, nombre, montoD, posSel!!, null)
                        Toast.makeText(context, "Puja enviada", Toast.LENGTH_SHORT).show()
                        vm.cargarPujas()
                        nombre = ""; monto = ""; posSel = null
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Enviar puja")
                }
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    vm.finalizarSubasta(subastaId)
                    Toast.makeText(context, "Subasta finalizada", Toast.LENGTH_SHORT).show()
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                    Text("Finalizar Subasta")
                }

            } else {
                // Cuando la subasta no esté activa, mostrar el ganador
                // Observa cambios en detalleSubasta para disparar ganador
                LaunchedEffect(det) {
                    vm.consultarGanador(subastaId)
                }
                ganador?.let {
                    Spacer(Modifier.height(20.dp))
                    Text("Ganador de la subasta", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(Modifier.height(8.dp))
                    Text("Nombre: ${it.nombre}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text("Monto: $${it.montoGanador}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                } ?: run {
                    Spacer(Modifier.height(20.dp))
                    Text("Calculando ganador...", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

