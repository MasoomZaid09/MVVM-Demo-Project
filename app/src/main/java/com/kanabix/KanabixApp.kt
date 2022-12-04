package com.kanabix

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KanabixApp : Application(){

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
//
//        val networkInspectorConfiguration = ConnectionBuddyConfiguration.Builder(this).build()
//        ConnectionBuddy.getInstance().init(networkInspectorConfiguration)
//
//    }

}