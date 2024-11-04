package com.example.papalote_app.navigation

sealed class Screen(val route: String) {
    object Events: Screen("Events")
    object Favorites: Screen("Favorites")
    object Map: Screen("Map")
    object Profile: Screen("Profile")
    object QR: Screen("QR")
    object Register: Screen("Register")
    object SignIn: Screen("SignIn")
}