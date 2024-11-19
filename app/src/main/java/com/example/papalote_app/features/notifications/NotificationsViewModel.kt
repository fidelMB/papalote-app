package com.example.papalote_app.features.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.papalote_app.R
import com.example.papalote_app.model.Event

class NotificationsViewModel : ViewModel() {
    fun sendEventNotifications(context: Context, userEvents: List<Event>) {
        for (event in userEvents) {
            if (event.isNotified) {
                val notificationManager = context.getSystemService(NotificationManager::class.java)

                val icon = when (event.zone) {
                     "Expreso" -> R.drawable.expreso
                     "Soy" -> R.drawable.soy
                     "Comunico" -> R.drawable.comunico
                     "Comprendo" -> R.drawable.comprendo
                     "Pertenezco" -> R.drawable.pertenezco
                     else -> R.drawable.pequenos
                }

                val notification = NotificationCompat.Builder(context, MyApp.CHANNEL_ID)
                    .setContentTitle(event.name)
                    .setContentText("No lo olvides, el evento será el ${event.date}")
                    .setSmallIcon(icon)
                    .setAutoCancel(true)
                    .build()
                notificationManager.notify(event.name.hashCode(), notification)
            }
        }
    }
}