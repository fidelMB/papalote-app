// navigation/MainNavigation.kt
package com.example.papalote_app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.papalote_app.model.UserData
import com.example.papalote_app.components.ProfileViewModel
import com.example.papalote_app.model.UserProfile
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
    val profileViewModel: ProfileViewModel = viewModel()

    // Cargar el índice de la imagen de perfil desde Firestore al iniciar
    LaunchedEffect(Unit) {
        firestore.collection("users")
            .document(userData.email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val profilePicture = document.getLong("profilePicture")?.toInt() ?: 1
                    userData.profilePicture = profilePicture
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainNavigation", "Error loading profile picture", e)
            }
    }

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
                userData = userData,
                navController = navController,
                user = profileViewModel.user.collectAsState().value,
                onImageChange = { uri ->
                    // Si decides manejar imágenes desde la galería, necesitas implementar esto.
                    // profileViewModel.updateProfilePicture(uri)
                },
                onDefaultImageSelect = { selectedImageIndex ->
                    profileViewModel.updateDefaultImage(selectedImageIndex)
                    Log.d("Profile", "Default image updated to index: $selectedImageIndex")
                }
            )
        }

    }
}
