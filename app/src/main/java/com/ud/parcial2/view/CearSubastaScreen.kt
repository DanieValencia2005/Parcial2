package com.ud.parcial2.view

// Importaciones necesarias para Compose, navegación, carga de imágenes, y red
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ud.parcial2.viewmodel.SubastaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

@Composable
fun CrearSubastaScreen(navController: NavController, vm: SubastaViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Estados para título, descripción, URL y URI de la imagen
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para seleccionar imagen de la galería
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
        }
    }

    // Función suspendida para subir imagen a Cloudinary y obtener URL pública
    suspend fun subirImagenACloudinary(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver
            val inputStream = resolver.openInputStream(uri) ?: return@withContext null

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "imagen.jpg", inputStream.readBytes().toRequestBody(null))
                .addFormDataPart("upload_preset", "Parcial2") // ← preset configurado en Cloudinary
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/dwi6lodtp/image/upload") // ← URL base de Cloudinary
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()
            val json = response.body?.string() ?: return@withContext null

            // Extrae la URL segura de la imagen del JSON de respuesta
            val url = Regex("\"secure_url\":\"(.*?)\"").find(json)
                ?.groupValues?.get(1)
                ?.replace("\\/", "/")

            url
        } catch (e: Exception) {
            null
        }
    }

    // Interfaz de usuario para crear una subasta
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

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar imagen de galería
        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Seleccionar imagen de galería")
        }

        // Muestra la imagen seleccionada
        imageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Botón para crear la subasta
        Button(
            onClick = {
                if (titulo.isBlank() || descripcion.isBlank()) {
                    Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                } else {
                    if (imageUri != null) {
                        // Si se seleccionó imagen, primero se sube a Cloudinary
                        scope.launch {
                            val urlSubida = subirImagenACloudinary(imageUri!!)
                            if (urlSubida != null) {
                                imageUrl = urlSubida
                                vm.crearSubasta(titulo, descripcion, "activa", urlSubida)
                                navController.navigate("lista")
                            } else {
                                Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Si no hay imagen, se crea la subasta sin imagen
                        vm.crearSubasta(titulo, descripcion, "activa", null)
                        navController.navigate("lista")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear subasta")
        }
    }
}
