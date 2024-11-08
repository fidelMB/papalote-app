package com.example.papalote_app.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.papalote_app.components.EventCard
import com.example.papalote_app.model.getEvents

@Composable
fun Events(navController: NavController) {
    BackHandler {
        navController.popBackStack()
    }

    LazyColumn {
        items(items = getEvents()) { event ->
            EventCard(event)
        }

    }

    Text("Eventos")
}