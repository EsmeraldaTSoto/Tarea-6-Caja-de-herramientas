package com.example.cajaherramientas

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PokemonScreen() {
    var nombre by remember { mutableStateOf("pikachu") }
    var p by remember { mutableStateOf<JsonObject?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) { onDispose { player?.release() } }

    fun sonido(url: String) {
        player?.release()
        player = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener { it.start() }
            prepareAsync()
        }
    }

    Pagina {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Pokémon") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            scope.launch {
                loading = true; error = null
                try {
                    p = Net.api.obj("https://pokeapi.co/api/v2/pokemon/${nombre.trim().lowercase()}")
                } catch (e: Exception) {
                    p = null
                    error = "Pokémon no encontrado."
                }
                loading = false
            }
        }) { Text("Buscar") }

        Estado(loading, error)

        p?.takeIf { !loading }?.let { x ->
            val sprites = x.getAsJsonObject("sprites")
            val img = sprites.getAsJsonObject("other")
                ?.getAsJsonObject("official-artwork")?.get("front_default").str()
                ?: sprites.get("front_default").str()
            val grito = x.getAsJsonObject("cries")?.get("latest").str()

            AsyncImage(model = img, contentDescription = "Pokémon", modifier = Modifier.size(220.dp))
            Text(x.get("name").asString.uppercase(), fontSize = 24.sp)
            Text("Experiencia base: ${x.get("base_experience").str() ?: "-"}")
            Text("Habilidades:")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                x.getAsJsonArray("abilities").forEach { a ->
                    AssistChip(
                        onClick = {},
                        label = { Text(a.asJsonObject.getAsJsonObject("ability").get("name").asString) }
                    )
                }
            }
            if (grito != null) {
                Button(onClick = { sonido(grito) }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Reproducir sonido")
                }
            }
        }
    }
}