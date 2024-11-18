package com.example.papalote_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val name: String,
    val email: String,
    val phone: String,
    val url: String,
) : Parcelable

@Parcelize
data class UserData(
    var userId: String = "",
    val birthDate: String = "Cargando...",
    val createdAt: Long = 0L,
    val email: String = "Cargando...",
    val fullName: String = "Cargando...",
    val gender: String = "Cargando...",
    var profilePicture: Int = 1,
    var activities: List<Activity> = emptyList(),
    var events: List<Event> = emptyList()
) : Parcelable