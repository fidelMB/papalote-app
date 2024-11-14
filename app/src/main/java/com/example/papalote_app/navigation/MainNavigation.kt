// navigation/MainNavigation.kt
package com.example.papalote_app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.papalote_app.components.ProfileViewModel
import com.example.papalote_app.model.UserProfile
import com.example.papalote_app.screens.*

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onSignOut: () -> Unit
) {
    val profileViewModel: ProfileViewModel = viewModel()

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
            Profile(
                navController = navController,
                user = profileViewModel.user.collectAsState().value,
                onSignOut = onSignOut,
                onImageChange = { uri ->
                    profileViewModel.updateProfilePicture(uri) // Use the correct function name here
                    Log.d("Profile", "Profile picture updated: ${uri.toString()}")
                }
            )
        }
    }
}
