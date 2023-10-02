package com.example.foregroundservicetutorial

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import java.nio.file.attribute.AclEntry.Builder

class MyApplication : Application() {
    companion object{
        val CHANNEL_ID = "channel_id"
    }
    override fun onCreate() {
        super.onCreate()
        createChannelNotification()
    }

    private fun createChannelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val mChannel = NotificationChannel(CHANNEL_ID, "Channel example", NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}