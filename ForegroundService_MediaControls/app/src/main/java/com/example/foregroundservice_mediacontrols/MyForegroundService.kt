package com.example.foregroundservice_mediacontrols

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.foregroundservice_mediacontrols.MyApplication.Companion.CHANNEL_ID
import java.util.Date

class MyForegroundService : Service() {

    companion object{
        val PAUSE = 0
        val RESUME = 1
        val CLEAR = 2
        val START = 3
    }

    private var mediaPlayer : MediaPlayer? = null
    private var isPlaying = false
    private lateinit var mSong: Song
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("foreground service", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras
        if(bundle != null){
            val song = bundle.get("object_song") as Song?
            if(song != null){
                mSong = song
                startMusic(song)
                sendNotificationMedia(song)
            }

        }

        val actionMusic = intent?.getIntExtra("action_music_service", 0)
        if (actionMusic != null) {
            handleActionMusic(actionMusic)
        }
        return START_NOT_STICKY
    }

    private fun startMusic(song: Song) {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(applicationContext, song.resource)
        }
        mediaPlayer?.start()
        isPlaying = true
        sendActionToActivity(START)
    }

    private fun handleActionMusic(action: Int){
        when(action){
            PAUSE -> pauseMusic()
            RESUME -> resumeMusic()
            CLEAR -> {
                stopSelf()
                sendActionToActivity(CLEAR)
            }
        }
    }


    private fun resumeMusic() {
        if(mediaPlayer != null && !isPlaying){
            mediaPlayer?.start()
            isPlaying = true
            sendNotificationMedia(mSong)
            sendActionToActivity(RESUME)
        }
    }

    private fun pauseMusic(){
        if(mediaPlayer != null && isPlaying){
            mediaPlayer?.pause()
            isPlaying = false
            sendNotificationMedia(mSong)
            sendActionToActivity(PAUSE)
        }
    }

//    private fun sendNotification(song: Song) {
//        val intent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val bitmap = BitmapFactory.decodeResource(resources, song.image)
//
//        //remote view custom
//        val remoteViews = RemoteViews(packageName, R.layout.layout_custom_notification)
//        remoteViews.setTextViewText(R.id.tv_title_song, song.title)
//        remoteViews.setTextViewText(R.id.tv_singer_song, song.singer)
//        remoteViews.setImageViewBitmap(R.id.img_song, bitmap)
//
//        remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_pause)
//
//        if(isPlaying) {
//            //xu ly su kien tren notification
//            remoteViews.setOnClickPendingIntent(
//                R.id.img_play_or_pause,
//                getPendingIntent(this, PAUSE)
//            )
//            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_pause)
//        } else{
//            remoteViews.setOnClickPendingIntent(
//                R.id.img_play_or_pause,
//                getPendingIntent(this, RESUME)
//            )
//            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.ic_play)
//        }
//
//        remoteViews.setOnClickPendingIntent(
//            R.id.img_close,
//            getPendingIntent(this, CLEAR)
//        )
//
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setContentIntent(pendingIntent)
//            .setCustomContentView(remoteViews)
//            .build()
//
//        // start foreground service
//        startForeground(getNotificationId(), notification)
//
//        // stop service
//        // dừng service ngay lập tức mà không cần đợi 1 khoảng thời gian cố định.
//        // stopSelf()
//    }

    private fun sendNotificationMedia(song: Song) {
        val bitmap = BitmapFactory.decodeResource(resources, mSong.image)
        val mediaSession = MediaSessionCompat(this, "mediaSession")


        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setSubText("MyLinh")
            .setContentTitle(mSong.title)
            .setContentText(mSong.singer)
            .setLargeIcon(bitmap)
            // Apply the media style template.
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(3 /* #1: pause button \*/)
//                .setMediaSession(mediaSession.sessionToken)
            )

        if(isPlaying){
            // Add media control buttons that invoke intents in your media service
            notification
                .addAction(R.drawable.ic_skip_previous, "Previous", null) //#0
                .addAction(R.drawable.ic_pause, "Pause", getPendingIntent(this, PAUSE))            //#1
                .addAction(R.drawable.ic_skip_next, "Next", null)          //#2
                .addAction(R.drawable.ic_close, "Close", getPendingIntent(this, CLEAR))          //#3

        } else{
            notification
                .addAction(R.drawable.ic_skip_previous, "Previous", null) //#0
                .addAction(R.drawable.ic_play, "Pause", getPendingIntent(this, RESUME))            //#1
                .addAction(R.drawable.ic_skip_next, "Next", null)          //#2
                .addAction(R.drawable.ic_close, "Close", getPendingIntent(this, CLEAR))
        }

        val builtNotification = notification.build()
        startForeground(getNotificationId(), builtNotification)

//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(getNotificationId(), notification)
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent{
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        intent.putExtra("action_music", action)

        return PendingIntent.getBroadcast(context.applicationContext, action, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.e("foreground service", "onDestroy")

        if(mediaPlayer != null){
            mediaPlayer?.release()
            mediaPlayer = null
        }

    }

    private fun getNotificationId(): Int {
        return Date().time.toInt()
    }

    private fun sendActionToActivity(action: Int){
        val intent = Intent("send_data_to_activity")
        val bundle = Bundle()
        bundle.putSerializable("object_song", mSong)
        bundle.putBoolean("status_player", isPlaying)
        bundle.putInt("action_music", action)
        intent.putExtras(bundle)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}