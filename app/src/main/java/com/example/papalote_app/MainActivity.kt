// MainActivity.kt
package com.example.papalote_app

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.papalote_app.components.NavBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.papalote_app.features.auth.AuthNavigation
import com.example.papalote_app.features.auth.AuthUiState
import com.example.papalote_app.features.auth.AuthViewModel
import com.example.papalote_app.features.notifications.NotificationsViewModel
import com.example.papalote_app.navigation.MainNavigation
import com.example.papalote_app.ui.theme.PapaloteappTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing Firebase", e)
        }

        enableEdgeToEdge()
        setContent {
            PapaloteappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val firestore = Firebase.firestore
                    val viewModel: AuthViewModel = viewModel()
                    val authState by viewModel.authState.collectAsState()
                    val userData by viewModel.userData.collectAsState()
                    val notificationsViewModel = NotificationsViewModel()

                    when (authState) {
                        is AuthUiState.Success -> {
                            val navController = rememberNavController()
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = { NavBar(navController = navController) }
                            ) { innerPadding ->
                                userData?.let {
                                    MainNavigation(
                                        modifier = Modifier.padding(innerPadding),
                                        navController = navController,
                                        onSignOut = { viewModel.signOut() },
                                        userData = it,
                                        firestore = firestore
                                    )
                                    notificationsViewModel.sendEventNotifications(LocalContext.current, it.events)
                                }
                            }
                        }
                        is AuthUiState.NotAuthenticated,
                        is AuthUiState.Error,
                        is AuthUiState.ResetEmailSent -> {
                            AuthNavigation(
                                onAuthSuccess = { /* El estado se actualizará automáticamente */ }
                            )
                        }
                        is AuthUiState.Loading,
                        AuthUiState.Initial -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}