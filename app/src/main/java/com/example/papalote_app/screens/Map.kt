package com.example.papalote_app.screens

import com.example.papalote_app.R
import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.papalote_app.model.UserData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
fun Map(userData: UserData) {

    // Estados para las pestañas
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pisos = listOf("PB", "S1", "S2")

    var selectedTopTabIndex by remember { mutableIntStateOf(0) }
    val topTabs: List<Pair<String, Int>> = listOf(
        Pair("Expreso", R.drawable.expreso),
        Pair("Soy", R.drawable.soy),
        Pair("Comprendo", R.drawable.comprendo),
        Pair("Pertenezco", R.drawable.pertenezco),
        Pair("Pequeños", R.drawable.pequenos),
        Pair("Comunico", R.drawable.comunico)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight() // La altura se ajusta al contenido dentro de la Box
            .background(Color.White)
    ) {
        Column {
            // Contenido principal del mapa
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTabIndex) {
                    0 -> PisoContent(piso = 0) // Contenido del Piso PB
                    1 -> PisoContent(piso = 1) // Contenido del Piso S1
                    2 -> PisoContent(piso = 2) // Contenido del Piso S2
                }
            }
        }
        // Contenido principal
        Column (
            modifier = Modifier
                .background(Color.White)
        ){
            Text(
                text = "Mapa",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF1D1B20),
                    fontWeight = FontWeight(600)
                ),
                modifier = Modifier
                    .padding(32.dp, 16.dp, 32.dp, 0.dp)
                    .height(44.dp)
            )
            // Scrollable TabRow superior
            ScrollableTabRow(
                selectedTabIndex = selectedTopTabIndex,
                containerColor = Color.White,
                contentColor = Color.Black,
                edgePadding = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(60.dp),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTopTabIndex]),
                        color = when (selectedTopTabIndex) {
                            0 -> Color(0xFFF58220)
                            1 -> Color(0xFFDB1E36)
                            2 -> Color(0xFF853694)
                            3 -> Color(0xFFC0D330)
                            4 -> Color(0xFF009BA7)
                            5 -> Color(0xFF006D9E)
                            else -> Color.Cyan
                        },
                        height = 4.dp // Puedes ajustar el grosor del indicador
                    )
                }
            ) {
                topTabs.forEachIndexed { index, (title, iconRes) ->
                    Tab(
                        selected = selectedTopTabIndex == index,
                        onClick = { selectedTopTabIndex = index },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 8.dp)
                                )
                                Text(
                                    text = title,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    )
                }
            }


        }

        // TabRow inferior (debajo del mapa)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
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
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp) // Esquinas redondeadas
                    )
                    .shadow(8.dp, RoundedCornerShape(24.dp)) // Sombra
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

@Composable
fun InfoPopup(
    showPopup: Boolean,
    optionsWithImages: Map<String, Int>, // Map que asocia opciones con imágenes
    onDismiss: () -> Unit
) {
    val activityImage = painterResource(id = R.drawable.media)
//    val activityAreaIcon = painterResource(id = R.drawable.expreso)
    var expandedOptionIndex by remember { mutableStateOf<Int?>(null) }
    val options = optionsWithImages.keys.toList()
    val icons = remember { mutableStateListOf(R.drawable.ic_add) }
    val scrollState = rememberScrollState()

    if (showPopup) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp,
                        focusedElevation = 4.dp
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .height(400.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        options.forEachIndexed { index, option ->
                            if (icons.size <= index) {
                                icons.add(R.drawable.ic_add)
                            }
                            AnimatedVisibility(visible = expandedOptionIndex == null || expandedOptionIndex == index) {
                                Column {
                                    Column {
                                        Button(
                                            onClick = {
                                                expandedOptionIndex = if (expandedOptionIndex == index) null else index
                                                // Alternar entre los íconos
                                                icons[index] = if (icons[index] == R.drawable.ic_add) {
                                                    R.drawable.ic_remove
                                                } else {
                                                    R.drawable.ic_add
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth() // Fill the width of the popup
                                                .height(71.dp), // Set the height of the button
                                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                                            contentPadding = PaddingValues(0.dp),
                                            shape = RectangleShape
                                        ) {
                                            Column {
                                                Row(
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    // Icon and Text on the left
                                                    Row(modifier = Modifier.padding(start = 16.dp)) {
                                                        Image(
                                                            painter = painterResource(id = optionsWithImages[option] ?: R.drawable.comunico),
                                                            contentDescription = null,
                                                            modifier = Modifier
                                                                .height(30.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Column(
                                                        modifier = Modifier.weight(1f),
                                                        verticalArrangement = Arrangement.Center
                                                    ) {
                                                        Text(
                                                            text = option,
                                                            color = Color.Black,
                                                            fontSize = 16.sp,
                                                            textAlign = TextAlign.Start,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Text(
                                                            text = "Zona",
                                                            color = Color.Black
                                                        )
                                                    }
                                                    // Icon on the right
                                                    Image(
                                                        painter = painterResource(id = icons[index]),
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .align(Alignment.CenterVertically)
                                                            .padding(end = 16.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    AnimatedVisibility(visible = expandedOptionIndex == index) {
                                        Box {
                                            Column {
                                                Row {
                                                    Image(
                                                        painter = activityImage,
                                                        contentDescription = null,
                                                        modifier = Modifier.width(400.dp).height(188.dp)
                                                    )
                                                }
                                                Row (modifier = Modifier.padding(top = 20.dp)) {
                                                    Text(
                                                        text = "Relieve",
                                                        modifier = Modifier.padding(start = 16.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                                Row {
                                                    Text(
                                                        text = "Crea divertidas figuras de la naturaleza en nuestra pared de clavos.",
                                                        modifier = Modifier.padding(start = 16.dp)
                                                    )
                                                }
                                                Column { // Column with icons and button
                                                    Row( // Row for icons
                                                        verticalAlignment = Alignment.CenterVertically, // Align icons vertically
                                                        modifier = Modifier.padding(start = 16.dp) // Add padding to the start
                                                    ) {
                                                        // Estados para controlar el ícono activo (filled o outlined)
                                                        var isFavoriteFilled by remember { mutableStateOf(false) }
                                                        var isThumbUpFilled by remember { mutableStateOf(false) }
                                                        var isThumbDownFilled by remember { mutableStateOf(false) }

                                                        Button(
                                                            onClick = { isFavoriteFilled = !isFavoriteFilled },
                                                            modifier = Modifier.size(40.dp), // Ajusta el tamaño del botón
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                                            contentPadding = PaddingValues(0.dp) // Elimina el relleno interno
                                                        ) {
                                                            Icon(
                                                                painter = if (isFavoriteFilled) painterResource(id = R.drawable.favorite_filled) else painterResource(id = R.drawable.favorite),
                                                                contentDescription = null,
                                                                modifier = Modifier.size(24.dp), // Ajusta el tamaño del ícono
                                                                tint = Color.Black // color negro
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(8.dp)) // Añade espacio entre los botones

                                                        Button(
                                                            onClick = {
                                                                isThumbUpFilled = !isThumbUpFilled
                                                                if (isThumbUpFilled) isThumbDownFilled = false // Desactiva el otro botón
                                                                      },
                                                            modifier = Modifier.size(40.dp),
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                                            contentPadding = PaddingValues(0.dp)
                                                        ) {
                                                            Icon(
                                                                painter = if (isThumbUpFilled) painterResource(id = R.drawable.thumb_up_filled) else painterResource(id = R.drawable.thumb_up),
                                                                contentDescription = null,
                                                                modifier = Modifier.size(24.dp),
                                                                tint = Color.Black
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(8.dp))

                                                        Button(
                                                            onClick = {
                                                                isThumbDownFilled = !isThumbDownFilled
                                                                if (isThumbDownFilled) isThumbUpFilled = false // Desactiva el otro botón
                                                                      },
                                                            modifier = Modifier.size(40.dp),
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                                            contentPadding = PaddingValues(0.dp)
                                                        ) {
                                                            Icon(
                                                                painter = if (isThumbDownFilled) painterResource(id = R.drawable.thumb_down_filled) else painterResource(id = R.drawable.thumb_down),
                                                                contentDescription = null,
                                                                modifier = Modifier.size(24.dp),
                                                                tint = Color.Black
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.padding(start = 135.dp)) // Espacio entre botones y el botón "Regresar"

//                                                        Button(
//                                                            onClick = { /* Acción para el botón Regresar */ },
//                                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC4D600))
//                                                        ) {
//                                                            Text("Listo", color = Color.Black)
//                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//fun toggleFavorite(isFavoriteFilled: MutableState<Boolean>) {
//    isFavoriteFilled.value = !isFavoriteFilled.value
//}
//
//fun toggleThumbUp(isThumbUpFilled: MutableState<Boolean>) {
//    isThumbUpFilled.value = !isThumbUpFilled.value
//}
//
//fun toggleThumbDown(isThumbDownFilled: MutableState<Boolean>) {
//    isThumbDownFilled.value = !isThumbDownFilled.value
//}


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

fun plantaBaja(): List<PolygonArea> {
    return listOf(
            PolygonArea(
                points = listOf(
                    Offset(393f, 330f),
                    Offset(392f, 423f),
                    Offset(379f, 423f),
                    Offset(379f, 437f),
                    Offset(392f, 438f),
                    Offset(393f, 456f),
                    Offset(703f, 456f),
                    Offset(702f, 332f)
                ),
                initialColor = Color.Gray,
                label = "Área de Alimentos Exterior",
                onClick = {  },
                initialOffset = Offset(-60f, 400f)
            ),
            PolygonArea(
            points = listOf(
                Offset(404f, 388f),
                Offset(404f, 399f),
                Offset(467f, 399f),
                Offset(467f, 453f),
                Offset(527f, 452f),
                Offset(527f, 428f),
                Offset(569f, 426f),
                Offset(570f, 453f),
                Offset(628f, 452f),
                Offset(629f, 400f),
                Offset(691f, 397f),
                Offset(691f, 387f),
                Offset(629f, 387f),
                Offset(629f, 333f),
                Offset(468f, 332f),
                Offset(467f, 385f)
            ),
            initialColor = Color.DarkGray,
            label = "Área de Alimentos Interior",
            onClick = {  },
            initialOffset = Offset(-60f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(152f, 54f),
                Offset(151f, 199f),
                Offset(457f, 200f),
                Offset(457f, 225f),
                Offset(572f, 225f),
                Offset(573f, 55f)
            ),
            initialColor = Color.Gray,
            label = "Área 2",
            onClick = {  },
            initialOffset = Offset(-60f, 400f)
        )
    )
}

fun sotano1(): List<PolygonArea> {
    return listOf(
        PolygonArea(
            points = listOf(
                Offset(138f, 93f), Offset(531f, 89f), Offset(534f, 353f), Offset(822f, 415f),
                Offset(850f, 577f), Offset(747f, 639f), Offset(664f, 801f), Offset(507f, 806f),
                Offset(282f, 504f), Offset(55f, 499f), Offset(56f, 331f), Offset(150f, 331f),
                Offset(151f, 291f), Offset(165f, 256f)
            ),
            initialColor = Color.Gray,
            label = "Background",
            onClick = { /* Acción para Background */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(655f, 411f), Offset(660f, 477f), Offset(676f, 490f), Offset(715f, 424f)
            ),
            initialColor = Color(0xFF489212),
            label = "Energía",
            onClick = { /* Acción para Energía */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(278f, 295f),
                Offset(288f, 316f),
                Offset(270f, 323f),
                Offset(291f, 325f),
                Offset(301f, 337f),
                Offset(290f, 342f),
                Offset(306f, 343f),
                Offset(330f, 364f),
                Offset(317f, 337f),
                Offset(322f, 316f),
                Offset(313f, 325f),
                Offset(303f, 318f),
                Offset(308f, 296f),
                Offset(296f, 310f)
            ),
            initialColor = Color.Cyan,
            label = "Monstruo de Aramberri",
            onClick = { /* Acción para Energía */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(597f, 689f), Offset(597f, 773f), Offset(662f, 772f), Offset(710f, 681f),
                Offset(708f, 665f), Offset(719f, 638f), Offset(647f, 676f), Offset(634f, 689f)
            ),
            initialColor = Color(0xFF55CDD0),
            label = "Submarino",
            onClick = { /* Acción para Submarino */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(164f, 361f), Offset(143f, 399f), Offset(94f, 399f), Offset(94f, 437f),
                Offset(243f, 439f), Offset(244f, 375f), Offset(223f, 361f)
            ),
            initialColor = Color(0xFFCCE9C6),
            label = "Tienda",
            onClick = { /* Acción para Tienda */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(301f, 215f), Offset(391f, 339f), Offset(486f, 301f), Offset(526f, 301f),
                Offset(523f, 130f)
            ),
            initialColor = Color(0xFFCCE9C6),
            label = "Sala de Exposiciones Temporales",
            onClick = { /* Acción para Sala de Exposiciones Temporales */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(264f, 222f), Offset(216f, 323f), Offset(223f, 340f), Offset(291f, 386f),
                Offset(302f, 425f), Offset(369f, 483f), Offset(410f, 471f), Offset(265f, 326f),
                Offset(264f, 314f), Offset(284f, 279f), Offset(294f, 280f), Offset(344f, 340f),
                Offset(370f, 393f), Offset(431f, 464f), Offset(466f, 453f), Offset(298f, 213f)
            ),
            initialColor = Color.DarkGray,
            label = "DarkedZone",
            onClick = { /* Acción para DarkedZone */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(164f, 156f), Offset(176f, 218f), Offset(186f, 201f), Offset(178f, 132f)
            ),
            initialColor = Color.DarkGray,
            label = "DarkedZone",
            onClick = { /* Acción para DarkedZone Poly */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(232f, 333f), Offset(212f, 367f), Offset(237f, 362f)
            ),
            initialColor = Color(0xffff8d2f),//light orange
            label = "Acceso al Museo",
            onClick = { /* Acción para Acceso al Museo */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(286f, 88f), Offset(288f, 125f), Offset(385f, 124f), Offset(384f, 88f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area3",
            onClick = { /* Acción para Area3 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(396f, 89f), Offset(398f, 114f), Offset(456f, 115f), Offset(456f, 89f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area7",
            onClick = { /* Acción para Area7 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(152f, 88f), Offset(152f, 112f), Offset(252f, 111f), Offset(252f, 88f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Acceso IMAX",
            onClick = { /* Acción para Acceso IMAX */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(80f, 358f), Offset(78f, 382f), Offset(99f, 383f), Offset(101f, 359f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area2",
            onClick = { /* Acción para Area2 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(186f, 449f), Offset(186f, 472f), Offset(207f, 474f), Offset(207f, 452f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area2",
            onClick = { /* Acción para Area2 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(111f, 362f), Offset(110f, 378f), Offset(127f, 379f), Offset(129f, 362f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area8",
            onClick = { /* Acción para Area8 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(402f, 126f), Offset(402f, 141f), Offset(415f, 141f), Offset(415f, 127f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area4",
            onClick = { /* Acción para Area4 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(191f, 139f), Offset(190f, 164f), Offset(209f, 163f), Offset(210f, 142f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area5",
            onClick = { /* Acción para Area5 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(195f, 190f), Offset(194f, 209f), Offset(216f, 209f), Offset(216f, 189f)
            ),
            initialColor = Color(0xFFED484B),// LightRed
            label = "Area6",
            onClick = { /* Acción para Area6 */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(395f, 344f), Offset(468f, 454f), Offset(414f, 479f), Offset(429f, 537f),
                Offset(441f, 527f), Offset(582f, 479f), Offset(667f, 524f), Offset(684f, 524f),
                Offset(700f, 510f), Offset(727f, 465f), Offset(775f, 459f), Offset(793f, 557f),
                Offset(827f, 580f), Offset(839f, 563f), Offset(816f, 434f), Offset(728f, 413f),
                Offset(681f, 491f), Offset(658f, 482f), Offset(649f, 411f), Offset(531f, 382f),
                Offset(523f, 373f), Offset(525f, 308f), Offset(486f, 305f)
            ),
            initialColor = Color.DarkGray, //DarkGreen
            label = "DarkedZone",
            onClick = { /* Acción para DarkedZone */ },
            initialOffset = Offset(20f, 400f)
        ),
        PolygonArea(
            points = listOf(
                Offset(330f, 543f), Offset(427f, 544f), Offset(426f, 557f), Offset(539f, 685f),
                Offset(592f, 690f), Offset(593f, 775f), Offset(502f, 774f), Offset(484f, 766f)
            ),
            initialColor = Color.DarkGray,
            label = "DarkedZone",
            onClick = { /* Acción para DarkedZone */ },
            initialOffset = Offset(20f, 400f)
        )
    )
}

fun sotano2(): List<PolygonArea> {
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
            initialColor = Color(0xFF7946A4), //#7946A4
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
            initialColor = Color(0xFFFF311B),
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
            initialColor = Color(0xFFCCE9C6),
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
            initialColor = Color(0xFFFFDA9B),
            label = "Dinosaurios",
            onClick = { /* Acción visual sin clic */ },
//            initialOffset = Offset(90f, 1130f)
            initialOffset = Offset(-60f, 400f)
        )
    )
}

@Composable
fun MapaInteractivo(areas: List<PolygonArea>) {
    val scale = remember { mutableFloatStateOf(1.5f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()
    val areaColors = remember { areas.map { mutableStateOf(it.initialColor) } }

    var showPopup by remember { mutableStateOf(false) }
    var selectedAreaLabel by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D600)) // Fondo Mapa interactivo verde 0xFFC4D600
            .clipToBounds() // Recorta cualquier parte del mapa que sobresalga
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    scale.floatValue = (scale.floatValue * zoom).coerceIn(1f, 5f)
                    val adjustedPan = pan * scale.floatValue
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
                            if (area.label !in listOf(
                                    "Background",
                                    "DarkedZone",
                                    "Área de Alimentos Exterior"
                                )
                            ) {
                                val transformedPoints = area.points.map { point ->
                                    Offset(
                                        x = (point.x + area.initialOffset.x) * scale.floatValue + offset.value.x,
                                        y = (point.y + area.initialOffset.y) * scale.floatValue + offset.value.y
                                    )
                                }
                                if (isPointInPolygon(tapOffset, transformedPoints)) {
                                    coroutineScope.launch {
                                        areaColors[index].value = Color.LightGray
                                        delay(300)
                                        areaColors[index].value = area.initialColor
                                    }
                                    showPopup = true
                                    selectedAreaLabel = area.label
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
                            x = (point.x + area.initialOffset.x) * scale.floatValue + offset.value.x,
                            y = (point.y + area.initialOffset.y) * scale.floatValue + offset.value.y
                        )
                    }
                    moveTo(transformedPoints.first().x, transformedPoints.first().y)
                    transformedPoints.drop(1).forEach { lineTo(it.x, it.y) }
                    close()
                }
                drawPath(path = path, color = areaColors[index].value.copy(alpha = 0.9f))
            }
        }
        InfoPopup(
            showPopup = showPopup,
            optionsWithImages = mapOf(
                "Expreso" to R.drawable.expreso,
                "Soy" to R.drawable.soy,
                "Comprendo" to R.drawable.comprendo,
                "Pertenezco" to R.drawable.pertenezco,
                "Pequeños" to R.drawable.pequenos,
                "Comunico" to R.drawable.comunico,
                "Innovo" to R.drawable.expreso, //Actividad de prueba
                "Juego" to R.drawable.expreso, //Actividad de prueba
                "Descubro" to R.drawable.expreso //Actividad de prueba
            ),
            onDismiss = { showPopup = false }
        )

        // Botones de Zoom In y Zoom Out en la parte inferior derecha
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            FloatingActionButton(
//                onClick = {
//                    scale.floatValue = (scale.floatValue * 1.2f).coerceIn(1f, 5f)
//                },
//                modifier = Modifier.size(50.dp),
//                containerColor = Color.White
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_zoom_in), // Asegúrate de que este recurso exista
//                    contentDescription = "Zoom In",
//                    tint = Color.Black
//                )
//            }
//
//            FloatingActionButton(
//                onClick = {
//                    scale.floatValue = (scale.floatValue / 1.2f).coerceIn(1f, 5f)
//                },
//                modifier = Modifier.size(50.dp),
//                containerColor = Color.White
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_zoom_out), // Asegúrate de que este recurso exista
//                    contentDescription = "Zoom Out",
//                    tint = Color.Black
//                )
//            }
//        }

    }
}

@Composable
fun PisoContent(piso: Int) {
    val areas = when (piso) {
        0 -> plantaBaja() // Contenido del Piso 1
        1 -> sotano1() // Contenido del Piso 2
        2 -> sotano2() // Contenido del Piso 3
        else -> emptyList()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Llama a la función que contiene la lógica de mapa y polígonos con las áreas del piso actual
        MapaInteractivo(areas = areas)
    }
}

