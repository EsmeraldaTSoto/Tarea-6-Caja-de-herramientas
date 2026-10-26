package com.example.cajaherramientas

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.google.gson.JsonElement

fun JsonElement?.str(): String? =
    if (this == null || isJsonNull) null else asString

fun limpiar(s: String): String =
    HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().trim()

fun abrir(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

@Composable
fun Pagina(content: @Composable ColumnScope.() -> Unit) = Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    content = content
)

@Composable
fun Estado(loading: Boolean, error: String?) {
    if (loading) CircularProgressIndicator()
    error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
}