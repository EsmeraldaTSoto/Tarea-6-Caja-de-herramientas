package com.example.cajaherramientas

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import com.google.gson.JsonObject

fun descripcionClima(c: Int) = when {
    c == 0 -> "Despejado"
    c <= 3 -> "Parcialmente nublado"
    c <= 48 -> "Neblina"
    c <= 57 -> "Llovizna"
    c <= 67 -> "Lluvia"
    c <= 82 -> "Chubascos"
    c >= 95 -> "Tormenta"
    else -> "Nublado"
}

@Composable
fun ClimaScreen() {
    var d by remember { mutableStateOf<JsonObject?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            d = Net.api.obj(
                "https://api.open-meteo.com/v1/forecast?latitude=18.4861&longitude=-69.9312" +
                        "&current_weather=true&daily=weathercode,temperature_2m_max,temperature_2m_min," +
                        "precipitation_probability_max&timezone=America/Santo_Domingo"
            )
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        }
        loading = false
    }

    Pagina {
        Estado(loading, error)
        d?.let { x ->
            val dia = x.getAsJsonObject("daily")
            Text("Santo Domingo, RD", fontSize = 20.sp)
            Text("${x.getAsJsonObject("current_weather").get("temperature").asString}°C", fontSize = 56.sp)
            Text(descripcionClima(dia.getAsJsonArray("weathercode")[0].asInt), fontSize = 22.sp)
            Text("Máxima: ${dia.getAsJsonArray("temperature_2m_max")[0].asString}°C")
            Text("Mínima: ${dia.getAsJsonArray("temperature_2m_min")[0].asString}°C")
            Text("Prob. de lluvia: ${dia.getAsJsonArray("precipitation_probability_max")[0].asString}%")
            Text("Fecha: ${dia.getAsJsonArray("time")[0].asString}")
        }
    }
}