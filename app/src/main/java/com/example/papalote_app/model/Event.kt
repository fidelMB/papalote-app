package com.example.papalote_app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val image: String = "",
    val zone: String = "",
    var isNotified: Boolean = false
) : Parcelable