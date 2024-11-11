// navigation/MainNavigation.kt
package com.example.papalote_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.papalote_app.model.UserProfile
import com.example.papalote_app.screens.*

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onSignOut: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.QR.route,
        modifier = modifier
    ) {
        composable(Screen.Events.route) {
            Events(navController = navController)
        }

        composable(Screen.Map.route) {
            Map(navController = navController)
        }

        composable(Screen.QR.route) {
            QR(navController = navController)
        }

        composable(Screen.Favorites.route) {
            Favorites()
        }

        composable(Screen.Profile.route) {
            val user = UserProfile(name = "John Doe", email = "johndoe@example.com", phone = "123-456-7890", url="img")
            Profile(
                navController = navController,
                onSignOut = onSignOut,
                user = user
            )
        }
    }
}