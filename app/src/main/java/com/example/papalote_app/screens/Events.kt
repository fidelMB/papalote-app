package com.example.papalote_app.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.papalote_app.components.EventCard
import com.example.papalote_app.model.getEvents

@Composable
fun Events(navController: NavController) {
    BackHandler {
        navController.popBackStack()
    }

    Column ()
    {
        Text(
            text = "Eventos",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF1D1B20),
                fontWeight = FontWeight(600)
            ),
            modifier = Modifier.padding(32.dp, 16.dp, 32.dp, 16.dp)
        )
        LazyColumn {
            items(items = getEvents()) { event ->
                EventCard(event)
            }

        }
    }
}