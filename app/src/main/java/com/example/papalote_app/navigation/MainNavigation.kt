// navigation/MainNavigation.kt
package com.example.papalote_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            Favorites(navController = navController)
        }

        composable(Screen.Profile.route) {
            Profile(
                navController = navController,
                onSignOut = onSignOut
            )
        }
    }
}