package com.example.papalote_app.features.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.papalote_app.R
import com.example.papalote_app.model.UserData

class NotificationsViewModel : ViewModel() {
    fun sendEventNotifications(context: Context, userData: UserData) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(context, MyApp.CHANNEL_ID)
            .setContentTitle("Titulo prueba")
            .setContentText("Texto prueba")
            .setSmallIcon(R.drawable.soy)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }
}