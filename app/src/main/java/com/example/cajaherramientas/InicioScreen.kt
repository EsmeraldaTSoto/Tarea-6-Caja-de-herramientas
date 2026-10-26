package com.example.cajaherramientas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InicioScreen() = Pagina {
    Image(
        painter = painterResource(R.drawable.caja),
        contentDescription = "Caja de herramientas",
        modifier = Modifier.fillMaxWidth().height(280.dp),
        contentScale = ContentScale.Fit
    )
    Text("Una app para varias cosas", fontSize = 22.sp)
    Text("Abre el menú para ver las herramientas.")
}