// navigation/MainNavigation.kt
package com.example.papalote_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.papalote_app.model.UserData
import com.example.papalote_app.screens.*
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onSignOut: () -> Unit,
    userData: UserData,
    firestore: FirebaseFirestore
) {
    NavHost(
        navController = navController,
        startDestination = Screen.QR.route,
        modifier = modifier
    ) {
        composable(Screen.Events.route) {
            Events(
                userData = userData,
                firestore = firestore
            )
        }

        composable(Screen.Map.route) {
            Map(
                userData = userData
            )
        }

        composable(Screen.QR.route) {
            QR(
                userData = userData
            )
        }

        composable(Screen.Favorites.route) {
            Favorites(
                userData = userData
            )
        }

        composable(Screen.Profile.route) {
            Profile(
                onSignOut = onSignOut,
                userData = userData
            )
        }
    }
}