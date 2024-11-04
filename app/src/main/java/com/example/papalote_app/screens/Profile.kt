package com.example.papalote_app.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Profile(navController: NavController) {
    BackHandler {
        navController.popBackStack()
    }

    Text("Perfil")
}