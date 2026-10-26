package com.example.cajaherramientas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { App() } }
    }
}

data class Destino(val ruta: String, val titulo: String, val icono: ImageVector)

val destinos = listOf(
    Destino("inicio", "Inicio", Icons.Default.Home),
    Destino("genero", "Género", Icons.Default.Face),
    Destino("persona", "Persona aleatoria", Icons.Default.Person),
    Destino("universidades", "Universidades", Icons.Default.School),
    Destino("clima", "Clima RD", Icons.Default.WbSunny),
    Destino("pokemon", "Pokémon", Icons.Default.Star),
    Destino("noticias", "Noticias WordPress", Icons.Default.Public),
    Destino("acerca", "Acerca de", Icons.Default.Info)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val nav = rememberNavController()
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val actual = nav.currentBackStackEntryAsState().value?.destination?.route ?: "inicio"

    ModalNavigationDrawer(
        drawerState = drawer,
        drawerContent = {
            ModalDrawerSheet {
                Text("Caja de Herramientas", Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge)
                destinos.forEach { d ->
                    NavigationDrawerItem(
                        icon = { Icon(d.icono, contentDescription = null) },
                        label = { Text(d.titulo) },
                        selected = d.ruta == actual,
                        onClick = {
                            scope.launch { drawer.close() }
                            nav.navigate(d.ruta) {
                                popUpTo("inicio")
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(destinos.firstOrNull { it.ruta == actual }?.titulo ?: "") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawer.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { pad ->
            NavHost(nav, startDestination = "inicio", modifier = Modifier.padding(pad)) {
                composable("inicio") { InicioScreen() }
                composable("genero") { GeneroScreen() }
                composable("persona") { PersonaScreen() }
                composable("universidades") { UniversidadesScreen() }
                composable("clima") { ClimaScreen() }
                composable("pokemon") { PokemonScreen() }
                composable("noticias") { NoticiasScreen() }
                composable("acerca") { AcercaScreen() }
            }
        }
    }
}