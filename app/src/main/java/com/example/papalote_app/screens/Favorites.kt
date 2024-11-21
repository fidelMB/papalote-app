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
import com.example.papalote_app.components.ActivityPopUp
import com.example.papalote_app.model.Activity
import com.example.papalote_app.model.getActivities
import com.example.papalote_app.components.FavoriteCard
import com.example.papalote_app.model.UserData


@Composable
fun Favorites(userData: UserData) {
    var popUp by remember { mutableStateOf<Activity?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        Column {
            Text(
                text = "Favoritos",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF1D1B20),
                    fontWeight = FontWeight(600)
                ),
                modifier = Modifier.padding(32.dp, 16.dp, 32.dp, 16.dp)
            )
            LazyColumn {
                items(items = userData.activities) { activity ->
                    FavoriteCard(activity = activity) { popUp = it } 

                }
            }
        }
        popUp?.let { activity ->
            ActivityPopUp(activity = activity) {
                popUp = null
            }
        }
    }
}