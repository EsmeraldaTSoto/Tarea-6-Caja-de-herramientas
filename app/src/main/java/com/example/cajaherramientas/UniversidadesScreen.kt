package com.example.cajaherramientas

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.JsonArray
import kotlinx.coroutines.launch

@Composable
fun UniversidadesScreen() {
    val context = LocalContext.current
    var pais by remember { mutableStateOf("Dominican Republic") }
    var lista by remember { mutableStateOf<JsonArray?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Pagina {
        OutlinedTextField(
            value = pais,
            onValueChange = { pais = it },
            label = { Text("País (en inglés)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            scope.launch {
                loading = true; error = null; lista = null
                try {
                    val r = Net.api.arr("https://adamix.net/proxy.php?country=${Uri.encode(pais.trim())}")
                    lista = r
                    if (r.size() == 0) error = "No se encontraron universidades."
                } catch (e: Exception) {
                    error = "Error: ${e.message}"
                }
                loading = false
            }
        }) { Text("Buscar") }

        Estado(loading, error)

        lista?.forEach { el ->
            val u = el.asJsonObject
            val dominios = u.getAsJsonArray("domains")?.joinToString(", ") { it.asString } ?: "-"
            val web = u.getAsJsonArray("web_pages")?.firstOrNull()?.asString
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(u.get("name").asString, style = MaterialTheme.typography.titleMedium)
                    Text("Dominio: $dominios")
                    if (web != null) {
                        Text(
                            web,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { abrir(context, web) }
                        )
                    }
                }
            }
        }
    }
}