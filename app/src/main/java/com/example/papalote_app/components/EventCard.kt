package com.example.papalote_app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.papalote_app.model.Event
import com.example.papalote_app.model.getEvents

@Composable
fun EventCard(event: Event = getEvents()[1]) {
    Card(
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(16.dp, 8.dp, 16.dp, 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(75.dp)
            ) {
                AsyncImage(
                    model = event.image,
                    contentDescription = "Event Image",
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp, 10.dp, 16.dp, 10.dp)
            ) {
                Text(
                    text = event.date.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy( // Smaller text size
                        color = Color(0xFF4A662C),
                        fontWeight = FontWeight(600)
                    )
                )
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleSmall.copy( // Smaller text size
                        color = Color(0xFF1D1B20),
                        fontWeight = FontWeight(500)
                    )
                )
                Text(
                    text = event.zone,
                    style = MaterialTheme.typography.bodySmall.copy( // Smaller text size
                        color = Color(0xFF615D67),
                        fontWeight = FontWeight(400)
                    )
                )
            }

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification",
                tint = Color.Black,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
                    .padding(end = 20.dp)
            )
        }
    }
}
