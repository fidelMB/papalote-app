package com.example.papalote_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Activity(
    val qr: String = "",
    val name: String = "",
    val zone: String = "",
    val description: String = "",
    val image: String = "",
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false,
    var isFavorite: Boolean = false
) : Parcelable

fun getActivities(): List<Activity> {
    return listOf(
        Activity(
            qr = "https://example.com",
            name = "Relieve",
            zone = "Zona Expreso",
            description = "Crea divertidas figuras de la naturaleza en nuestra pared de clavos.",
            image = "https://papalotemty.org.mx/wp-content/uploads/2023/03/relieve-exhibiciones-top.png"
        ),
        Activity(
            qr = "https://example.com",
            name = "Decidir",
            zone = "Zona Soy",
            description = "Existen productos amigables con el medio ambiente. ¡Descúbrelos!",
            image = "https://papalotemty.org.mx/wp-content/uploads/2023/03/decidir-exhibiciones-top.png"
        ),
        Activity(
            qr = "https://example.com",
            name = "Submarino",
            zone = "Zona Pequeños",
            description = "Lidera tu propia aventura acuática y descubre tesoros marinos.",
            image = "https://papalotemty.org.mx/wp-content/uploads/2023/03/submarino-exhibiciones-top.png"
        ),
        Activity(
            qr = "https://example.com",
            name = "Viento",
            zone = "Zona Pertenezco",
            description = "¿Imaginas lo que el viento puede transportar? Averígualo.",
            image = "https://papalotemty.org.mx/wp-content/uploads/2023/03/viento-exhibiciones-top.png"
        ),
        Activity(
            qr = "https://example.com",
            name = "Televisión",
            zone = "Zona Comunico",
            description = "Conviértete en un reportero ambiental en Telediario PAPALOTE.",
            image = "https://papalotemty.org.mx/wp-content/uploads/2023/03/television-exhibiciones-top.png"
        ),
        Activity(
            qr = "https://example.com",
            name = "Simulador",
            zone = "Zona Comprendo",
            description = "Observa una proyección de la Tierra vista desde el espacio y conoce sus fenómenos naturales.",
            image = "https://papalotemty.org.mx/wp-content/uploads/2023/03/simulador-exhibiciones-top.png"
        )
    )
}