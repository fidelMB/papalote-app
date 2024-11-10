package com.example.papalote_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.papalote_app.ui.theme.PapaloteappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PapaloteappTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { NavBar(navController = navController) }) { innerPadding ->
                    MainNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController
                    )
                }
            }
        }
    }
}


@Composable
fun MainNavigation(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.QR.route,
        modifier = modifier
    )
    {
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
            val user = UserProfile(name = "John Doe", email = "johndoe@example.com", phone = "123-456-7890", url="img")
            Profile(navController = navController, user=user)
        }
    }
}