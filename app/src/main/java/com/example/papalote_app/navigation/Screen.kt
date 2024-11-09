// navigation/Screen.kt
package com.example.papalote_app.navigation

sealed class Screen(val route: String) {
    object Events : Screen("events")
    object Map : Screen("map")
    object QR : Screen("qr")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")

    object SignIn : Screen("signin")
    object Register : Screen("register")
}