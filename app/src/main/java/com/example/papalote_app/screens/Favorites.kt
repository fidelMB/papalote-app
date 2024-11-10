package com.example.papalote_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.papalote_app.components.EventCard
import com.example.papalote_app.components.FavoriteCard
import com.example.papalote_app.model.getActivities
import com.example.papalote_app.model.getEvents

@Composable
fun Favorites(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        Column() {
            Text("Favoritos")
        }
        LazyColumn {
            items(items = getActivities()) { activity ->
                FavoriteCard(activity)
            }

        }
    }
}