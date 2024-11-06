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

