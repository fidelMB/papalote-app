// utils/Extensions.kt
package com.example.papalote_app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(format: String = Constants.DATE_FORMAT): Date? {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Date.format(format: String = Constants.DATE_FORMAT): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}

// Extensión para mapear códigos de género a texto legible
fun String.toGenderString(): String {
    return when (this) {
        Constants.Gender.MALE -> "Hombre"
        Constants.Gender.FEMALE -> "Mujer"
        Constants.Gender.OTHER -> "Prefiero no decirlo"
        else -> ""
    }
}