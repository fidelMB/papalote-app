package com.example.papalote_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Event(
    val name: String,
    val description: String,
    val date: String,
    val image: String,
    val zone: String
) : Parcelable


fun getEvents(): List<Event> {
    return listOf(
        Event(
            name = "Día de Descubrimiento Científico",
            description = "¡Explora las maravillas de la física, química y biología con divertidos experimentos prácticos!",
            date = "martes 15 nov 2024",
            image = "https://images.csmonitor.com/csm/2019/01/0121-L2MIX-touch-museum.jpg?alias=standard_900x600",
            zone = "Zona de Ciencias"
        ),
        Event(
            name = "Taller de Manualidades Ecológicas",
            description = "Aprende a hacer manualidades usando materiales reciclados y participa en una charla sobre sostenibilidad.",
            date = "martes 22 nov 2024",
            image = "https://example.com/images/eco_crafts.jpg",
            zone = "Zona de Arte y Reciclaje"
        ),
        Event(
            name = "Aventura en el Planetario",
            description = "Un recorrido guiado por nuestro mini planetario, donde los niños pueden aprender sobre el sistema solar y las estrellas.",
            date = "martes 2 dic 2024",
            image = "https://example.com/images/planetarium_adventure.jpg",
            zone = "Planetario"
        ),
        Event(
            name = "Paseo de Exploración Natural",
            description = "Únete a nosotros en una caminata por la naturaleza para aprender sobre las plantas, animales y cómo los ecosistemas trabajan juntos.",
            date = "martes 9 dic 2024",
            image = "https://example.com/images/nature_walk.jpg",
            zone = "Jardín de la Naturaleza"
        ),
        Event(
            name = "Taller de Alimentación Sostenible",
            description = "Una divertida sesión donde los niños aprenden sobre fuentes de alimentos sostenibles y prueban recetas simples y saludables.",
            date = "martes 16 dic 2024",
            image = "https://example.com/images/sustainable_food.jpg",
            zone = "Cocina Verde"
        )
    )
}