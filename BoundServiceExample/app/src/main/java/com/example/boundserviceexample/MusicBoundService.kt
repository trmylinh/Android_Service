package com.example.boundserviceexample

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicBoundService : Service(){

    private var mMediaPlayer: MediaPlayer? = null
    inner class MyBinder : Binder(){
       fun getMusicBoundService() : MusicBoundService{
           return this@MusicBoundService
       }
    }

    private val myBinder = MyBinder()

    override fun onBind(p0: Intent?): IBinder? {
        Log.e("MusicBoundService", "onBind")
        return myBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("MusicBoundService", "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("MusicBoundService", "onCreate")
    }

    override fun onDestroy() {
        Log.e("MusicBoundService", "onDestroy")
        super.onDestroy()
        if(mMediaPlayer != null){
            mMediaPlayer?.release()
        }
    }

    fun startMusic(){
        if(mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.mp3_song)
        }
        mMediaPlayer?.start()
    }
}