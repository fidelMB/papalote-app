package com.example.papalote_app.screens

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Datos de cada área poligonal
data class PolygonArea(
    val points: List<Offset>, // Puntos que definen el polígono
    val initialColor: Color, // Color inicial
    val label: String, // Etiqueta o nombre del área
    val onClick: () -> Unit, // Acción a ejecutar al hacer clic en el área
    val initialOffset: Offset = Offset.Zero // Desplazamiento inicial para ajustar la posición del polígono
)

@Composable
fun Map(navController: NavController) {
    BackHandler {
        navController.popBackStack()
    }

    // Estado para la pestaña seleccionada (representando los pisos)
    var selectedTabIndex by remember { mutableStateOf(0) }
    val pisos = listOf("PB", "S1", "S2")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCFDF68)) // Fondo verde general
    ) {

        // Contenido de cada piso, que incluye el mapa interactivo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp) // Espacio para el header
        ) {
            when (selectedTabIndex) {
                0 -> PisoContent(piso = 0) // Contenido del Piso PB
                1 -> PisoContent(piso = 1) // Contenido del Piso S1
                2 -> PisoContent(piso = 2) // Contenido del Piso S2
            }
        }

        // TabRow en la parte inferior, centrado y pequeño
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier
                    .width(200.dp) // Ancho del TabRow reducido
                    .height(48.dp) // Altura del TabRow ajustada
                    .align(Alignment.Center) // Centrado en la parte inferior
            ) {
                pisos.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontSize = 12.sp) }
                    )
                }
            }
        }
    }
}


// Función para verificar si un punto está dentro de un polígono
fun isPointInPolygon(point: Offset, vertices: List<Offset>): Boolean {
    var intersections = 0
    for (i in vertices.indices) {
        val v1 = vertices[i]
        val v2 = vertices[(i + 1) % vertices.size]

        if ((v1.y > point.y) != (v2.y > point.y)) {
            val slope = (point.y - v1.y) / (v2.y - v1.y)
            val xIntersection = v1.x + slope * (v2.x - v1.x)
            if (point.x < xIntersection) {
                intersections++
            }
        }
    }
    return intersections % 2 == 1
}

fun piso1Areas(): List<PolygonArea> {
    return listOf(
        PolygonArea(
            points = listOf(
                Offset(200f, 71f),
                Offset(227f, 136f),
                Offset(233f, 201f),
                Offset(228f, 254f),
                Offset(212f, 299f),
                Offset(197f, 322f),
                Offset(328f, 323f)
            ),
            initialColor = Color.Gray,
            label = "Zona 1 - Piso 1",
            onClick = { /* Acción para el área en Piso 1 */ }
        ),
        // Agrega más áreas específicas para el piso 1
    )
}

fun piso2Areas(): List<PolygonArea> {
    return listOf(
        PolygonArea(
            points = listOf(
                Offset(200f, 71f),
                Offset(227f, 136f),
                Offset(233f, 201f),
                Offset(228f, 254f),
                Offset(212f, 299f),
                Offset(197f, 322f),
                Offset(328f, 323f),
                Offset(676f, 805f),
                Offset(840f, 804f),
                Offset(924f, 639f),
                Offset(1029f, 573f),
                Offset(1005f, 407f),
                Offset(709f, 340f),
                Offset(705f, 130f),
                Offset(657f, 127f),
                Offset(469f, 199f),
                Offset(374f, 70f)
            ),
            initialColor = Color.Gray,
            label = "Background",
            onClick = { /* Acción visual sin clic */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(512f, 256f),
                Offset(669f, 196f),
                Offset(673f, 159f),
                Offset(700f, 159f),
                Offset(705f, 369f),
                Offset(743f, 384f),
                Offset(757f, 450f),
                Offset(713f, 470f),
                Offset(663f, 470f)
            ),
            initialColor = Color(0xFF800080),
            label = "Baylab",
            onClick = { /* Acción para Baylab */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(459f, 295f),
                Offset(467f, 316f),
                Offset(449f, 318f),
                Offset(470f, 324f),
                Offset(479f, 336f),
                Offset(466f, 342f),
                Offset(487f, 343f),
                Offset(506f, 361f),
                Offset(496f, 335f),
                Offset(502f, 317f),
                Offset(493f, 322f),
                Offset(484f, 314f),
                Offset(486f, 298f),
                Offset(479f, 309f)
            ),
            initialColor = Color.Cyan,
            label = "Monstruo de Aramberri",
            onClick = { /* Acción para Monstruo de Aramberri */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(793f, 643f),
                Offset(829f, 700f),
                Offset(844f, 769f),
                Offset(893f, 677f),
                Offset(890f, 664f),
                Offset(904f, 636f),
                Offset(953f, 606f),
                Offset(930f, 564f)
            ),
            initialColor = Color(0xFF55CDD0), //#55cdd0
            label = "Barco",
            onClick = { /* Acción para Barco */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(591f, 463f),
                Offset(569f, 463f),
                Offset(517f, 558f),
                Offset(672f, 771f),
                Offset(840f, 769f),
                Offset(829f, 701f),
                Offset(791f, 642f),
                Offset(755f, 648f),
                Offset(660f, 601f),
                Offset(666f, 588f),
                Offset(653f, 559f),
                Offset(658f, 537f),
                Offset(674f, 527f),
                Offset(694f, 527f),
                Offset(667f, 473f),
                Offset(618f, 487f)
            ),
            initialColor = Color.Red,
            label = "Decidir",
            onClick = { /* Acción para Decidir */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(670f, 473f),
                Offset(696f, 524f),
                Offset(766f, 527f),
                Offset(786f, 528f),
                Offset(826f, 538f),
                Offset(860f, 541f),
                Offset(886f, 528f),
                Offset(901f, 511f),
                Offset(914f, 496f),
                Offset(934f, 494f),
                Offset(952f, 507f),
                Offset(956f, 530f),
                Offset(951f, 549f),
                Offset(937f, 561f),
                Offset(956f, 603f),
                Offset(1016f, 570f),
                Offset(997f, 423f),
                Offset(912f, 403f),
                Offset(901f, 414f),
                Offset(744f, 385f),
                Offset(760f, 450f),
                Offset(717f, 474f)
            ),
            initialColor = Color(0xFFFF7923),
            label = "Relieve",
            onClick = { /* Acción para Relieve */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(69f, 73f),
                Offset(51f, 152f),
                Offset(50f, 219f),
                Offset(57f, 273f),
                Offset(68f, 322f),
                Offset(196f, 323f),
                Offset(216f, 285f),
                Offset(231f, 232f),
                Offset(231f, 187f),
                Offset(222f, 130f),
                Offset(191f, 70f)
            ),
            initialColor = Color.Cyan,
            label = "MegapantallaIMAX",
            onClick = { /* Acción para MegapantallaIMAX */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        // Polígonos sin interacción de clic
        PolygonArea(
            points = listOf(
                Offset(478f, 419f),
                Offset(510f, 389f),
                Offset(430f, 305f),
                Offset(475f, 284f),
                Offset(573f, 398f),
                Offset(583f, 423f),
                Offset(636f, 473f),
                Offset(660f, 466f),
                Offset(492f, 238f),
                Offset(370f, 286f),
                Offset(375f, 314f),
                Offset(459f, 379f)
            ),
            initialColor = Color.DarkGray,
            label = "DarkedZone",
            onClick = { /* Acción visual sin clic */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(670f, 588f),
                Offset(663f, 599f),
                Offset(760f, 648f),
                Offset(938f, 559f),
                Offset(955f, 536f),
                Offset(958f, 513f),
                Offset(932f, 492f),
                Offset(914f, 497f),
                Offset(902f, 517f),
                Offset(876f, 533f),
                Offset(852f, 541f),
                Offset(830f, 541f),
                Offset(789f, 531f),
                Offset(673f, 528f),
                Offset(664f, 540f),
                Offset(655f, 560f)
            ),
            initialColor = Color.DarkGray,
            label = "DarkedZone2",
            onClick = { /* Acción visual sin clic */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        )
    )
}

fun piso3Areas(): List<PolygonArea> {
    return listOf(
        PolygonArea(
            points = listOf(
                Offset(670f, 473f),
                Offset(696f, 524f),
                Offset(766f, 527f)
            ),
            initialColor = Color.Cyan,
            label = "Zona 1 - Piso 3",
            onClick = { /* Acción para el área en Piso 3 */ }
        ),
        // Agrega más áreas específicas para el piso 3
    )
}

@Composable
fun MapaInteractivo(areas: List<PolygonArea>) {
    val scale = remember { mutableStateOf(1.5f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()
    val areaColors = remember { areas.map { mutableStateOf(it.initialColor) } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCFDF68))
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    scale.value = (scale.value * zoom).coerceIn(1f, 5f)
                    val adjustedPan = pan * scale.value
                    offset.value += adjustedPan + (centroid * (1 - zoom))
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        areas.forEachIndexed { index, area ->
                            // Verifica si el área es decorativa y no debe reaccionar a clics
                            if (area.label !in listOf("Background", "DarkedZone", "DarkedZone2")) {
                                val transformedPoints = area.points.map { point ->
                                    Offset(
                                        x = (point.x + area.initialOffset.x) * scale.value + offset.value.x,
                                        y = (point.y + area.initialOffset.y) * scale.value + offset.value.y
                                    )
                                }
                                if (isPointInPolygon(tapOffset, transformedPoints)) {
                                    coroutineScope.launch {
                                        areaColors[index].value = Color.Yellow
                                        delay(300)
                                        areaColors[index].value = area.initialColor
                                    }
                                    area.onClick()
                                }
                            }
                        }
                    }
                }
        ) {
            areas.forEachIndexed { index, area ->
                val path = Path().apply {
                    val transformedPoints = area.points.map { point ->
                        Offset(
                            x = (point.x + area.initialOffset.x) * scale.value + offset.value.x,
                            y = (point.y + area.initialOffset.y) * scale.value + offset.value.y
                        )
                    }
                    moveTo(transformedPoints.first().x, transformedPoints.first().y)
                    transformedPoints.drop(1).forEach { lineTo(it.x, it.y) }
                    close()
                }
                drawPath(path = path, color = areaColors[index].value.copy(alpha = 0.9f))
            }
        }

        // Botones de Zoom In y Zoom Out en la parte inferior derecha
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    scale.value = (scale.value * 1.2f).coerceIn(1f, 5f)
                },
                modifier = Modifier.size(50.dp),
                containerColor = Color.White
            ) {
                Text("+", fontSize = 20.sp)
            }

            FloatingActionButton(
                onClick = {
                    scale.value = (scale.value / 1.2f).coerceIn(1f, 5f)
                },
                modifier = Modifier.size(50.dp),
                containerColor = Color.White
            ) {
                Text("-", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun PisoContent(piso: Int) {
    val areas = when (piso) {
        0 -> piso1Areas() // Contenido del Piso 1
        1 -> piso2Areas() // Contenido del Piso 2
        2 -> piso3Areas() // Contenido del Piso 3
        else -> emptyList()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Llama a la función que contiene la lógica de mapa y polígonos con las áreas del piso actual
        MapaInteractivo(areas = areas)
    }
}

