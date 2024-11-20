package com.example.papalote_app.features.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApp : Application() {
    companion object {
        const val CHANNEL_ID = "my_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios de eventos",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "Este canal de notificaciones se utiliza para recordar al ususario sobre los eventos del museo"
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}