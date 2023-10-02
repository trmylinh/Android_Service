package com.example.foregroundservicetutorial

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.foregroundservicetutorial.MyApplication.Companion.CHANNEL_ID
import java.util.Date

class MyForegroundService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("foreground service", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val strDataIntent: String? = intent?.getStringExtra("key_data")
        if (strDataIntent != null) {
            sendNotification(strDataIntent)
        }
        return START_NOT_STICKY
    }

    private fun sendNotification(string: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title Notification Foreground Service")
            .setContentText(string)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()

        // start foreground service
        startForeground(getNotificationId(), notification)

        // stop service
        // dừng service ngay lập tức mà không cần đợi 1 khoảng thời gian cố định.
        // stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("foreground service", "onDestroy")
    }

    private fun getNotificationId(): Int {
        return Date().time.toInt()
    }
}