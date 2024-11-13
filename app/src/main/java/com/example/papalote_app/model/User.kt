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
    val birthDate: String = "",
    val createdAt: Long = 0L,
    val email: String = "",
    val fullName: String = "",
    val gender: String = "",
    val activities: List<Activity> = emptyList(),
    val events: List<Event> = emptyList()
) : Parcelable