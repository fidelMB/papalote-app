package com.example.papalote_app.components

import android.content.Context

class ProfilePreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    // Guardar la imagen de perfil
    fun saveProfilePicture(userId: String, profilePicture: Int) {
        sharedPreferences.edit()
            .putInt("profile_picture_$userId", profilePicture)
            .apply()
    }

    // Cargar la imagen de perfil
    fun getProfilePicture(userId: String): Int {
        return sharedPreferences.getInt("profile_picture_$userId", 1) // 1 como predeterminado
    }
}
