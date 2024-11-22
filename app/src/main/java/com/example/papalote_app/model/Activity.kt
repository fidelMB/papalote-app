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