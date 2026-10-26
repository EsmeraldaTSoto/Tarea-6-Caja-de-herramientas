package com.example.cajaherramientas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AcercaScreen() {
    val context = LocalContext.current
    Pagina {
        Image(
            painter = painterResource(R.drawable.yo),
            contentDescription = "Mi foto",
            modifier = Modifier.size(160.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text("Esmeralda Soto", fontSize = 24.sp)
        Text("Estudiante de Desarrollo de Software | ITLA")
        Text("Correo: esmeraldatsoto.com")
        Text("Teléfono: 829-258-2847")
        Text(
            "github.com/EsmeraldaTSoto",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { abrir(context, "https://github.com/EsmeraldaTSoto") }
        )

    }
}