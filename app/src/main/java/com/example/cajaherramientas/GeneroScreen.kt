package com.example.cajaherramientas

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun GeneroScreen() {
    var nombre by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf<String?>(null) }
    var prob by remember { mutableStateOf(0.0) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Pagina {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            if (nombre.isBlank()) return@Button
            scope.launch {
                loading = true; error = null; genero = null
                try {
                    val d = Net.api.obj("https://api.genderize.io/?name=${Uri.encode(nombre.trim())}")
                    genero = d.get("gender").str()
                    prob = d.get("probability").str()?.toDouble() ?: 0.0
                    if (genero == null) error = "No se pudo predecir el género."
                } catch (e: Exception) {
                    error = "Error: ${e.message}"
                }
                loading = false
            }
        }) { Text("Predecir") }

        Estado(loading, error)

        genero?.let { g ->
            val masc = g == "male"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        if (masc) Color(0xFF1E88E5) else Color(0xFFE91E63),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (masc) "Masculino" else "Femenino", fontSize = 28.sp, color = Color.White)
                    Text("Probabilidad: ${(prob * 100).toInt()}%", color = Color.White)
                }
            }
        }
    }
}