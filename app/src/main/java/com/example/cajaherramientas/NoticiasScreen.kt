package com.example.cajaherramientas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.JsonArray

const val SITIO = "https://techcrunch.com"
const val LOGO = "https://techcrunch.com/wp-content/uploads/2015/02/cropped-cropped-favicon-gradient.png?w=270"

@Composable
fun NoticiasScreen() {
    val context = LocalContext.current
    var posts by remember { mutableStateOf<JsonArray?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            posts = Net.api.arr("$SITIO/wp-json/wp/v2/posts?per_page=3")
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        }
        loading = false
    }

    Pagina {
        AsyncImage(
            model = LOGO,
            contentDescription = "Logo de TechCrunch",
            modifier = Modifier.size(90.dp)
        )
        Text("TechCrunch", fontSize = 22.sp)
        Estado(loading, error)

        posts?.forEach { el ->
            val n = el.asJsonObject
            val titulo = limpiar(n.getAsJsonObject("title").get("rendered").asString)
            val resumen = limpiar(n.getAsJsonObject("excerpt").get("rendered").asString)
            val link = n.get("link").asString

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(titulo, style = MaterialTheme.typography.titleMedium)
                    Text(resumen)
                    TextButton(onClick = { abrir(context, link) }) {
                        Text("Visitar")
                    }
                }
            }
        }
    }
}