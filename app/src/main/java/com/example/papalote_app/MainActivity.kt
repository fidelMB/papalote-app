// MainActivity.kt
package com.example.papalote_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.papalote_app.components.NavBar
import com.example.papalote_app.model.UserProfile
import com.example.papalote_app.navigation.Screen
import com.example.papalote_app.screens.Events
import com.example.papalote_app.screens.Favorites
import com.example.papalote_app.screens.Map
import com.example.papalote_app.screens.Profile
import com.example.papalote_app.screens.QR
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.papalote_app.components.NavBar
import com.example.papalote_app.features.auth.AuthNavigation
import com.example.papalote_app.features.auth.AuthUiState
import com.example.papalote_app.features.auth.AuthViewModel
import com.example.papalote_app.navigation.MainNavigation
import com.example.papalote_app.ui.theme.PapaloteappTheme
import com.google.firebase.FirebaseApp

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
                    val viewModel: AuthViewModel = viewModel()
                    val authState by viewModel.authState.collectAsState()

                    when (authState) {
                        is AuthUiState.Success -> {
                            val navController = rememberNavController()
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                bottomBar = { NavBar(navController = navController) }
                            ) { innerPadding ->
                                MainNavigation(
                                    modifier = Modifier.padding(innerPadding),
                                    navController = navController,
                                    onSignOut = { viewModel.signOut() }
                                )
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

