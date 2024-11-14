package com.example.papalote_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.papalote_app.components.EventCard
import com.example.papalote_app.components.EventDialog
import com.example.papalote_app.model.Event
import com.example.papalote_app.model.getEvents

@Composable
fun Events(navController: NavController) {
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            // Screen title
            Text(
                text = "Eventos",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF1D1B20),
                    fontWeight = FontWeight(600)
                ),
                modifier = Modifier.padding(32.dp, 16.dp, 32.dp, 16.dp)
            )

            // lazy column that calls events from the event collection
            LazyColumn {
                items(items = getEvents()) { event ->
                    EventCard(event = event) { selectedEvent = it }
                }
            }
        }

        // Show EventDialog if an event is selected
        selectedEvent?.let { event ->
            EventDialog(event = event) {
                selectedEvent = null
            }
        }
    }
}