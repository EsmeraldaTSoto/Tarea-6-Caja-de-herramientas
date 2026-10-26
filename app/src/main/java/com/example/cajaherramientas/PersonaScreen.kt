package com.example.cajaherramientas

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

@Composable
fun PersonaScreen() {
    var u by remember { mutableStateOf<JsonObject?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun cargar() {
        scope.launch {
            loading = true; error = null
            try {
                val d = Net.api.obj("https://randomuser.me/api/?results=1")
                u = d.getAsJsonArray("results")[0].asJsonObject
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            }
            loading = false
        }
    }
    LaunchedEffect(Unit) { cargar() }

    Pagina {
        Button(onClick = { cargar() }) { Text("Nueva persona") }
        Estado(loading, error)
        u?.takeIf { !loading }?.let { p ->
            val name = p.getAsJsonObject("name")
            val dob = p.getAsJsonObject("dob")
            AsyncImage(
                model = p.getAsJsonObject("picture").get("large").asString,
                contentDescription = "Foto",
                modifier = Modifier.size(140.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text("Nombre: ${name.get("first").asString}")
            Text("Apellido: ${name.get("last").asString}")
            Text("Fecha de nacimiento: ${dob.get("date").asString.take(10)}")
            Text("Edad: ${dob.get("age").asInt} años")
        }
    }
}