package com.example.foregroundservice_mediacontrols

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.foregroundservice_mediacontrols.databinding.ActivityMainBinding
import com.example.foregroundservice_mediacontrols.MyForegroundService

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var mSong: Song
    private var isPlaying : Boolean = false


    private val broadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(content: Context?, intent: Intent?) {
            val bundle = intent?.extras ?: return

            mSong = bundle.get("object_song") as Song
            isPlaying = bundle.getBoolean("status_player")
            val actionMusic = bundle.getInt("action_music")

            handleLayoutMusic(actionMusic)

        }

    }

    private fun handleLayoutMusic(action: Int) {
        when(action){
            MyForegroundService.START -> {
                binding.layoutBottom.visibility = View.VISIBLE
                showInfoSong()
                setStatusButtonPlayOrPause()
            }
            MyForegroundService.PAUSE ->{
                setStatusButtonPlayOrPause()
            }
            MyForegroundService.RESUME ->{
                setStatusButtonPlayOrPause()
            }
            MyForegroundService.CLEAR ->{
                binding.layoutBottom.visibility = View.GONE
            }
        }
    }

    private fun showInfoSong(){
        if(mSong == null){
            return
        }
        binding.imgSong.setImageResource(mSong.image)
        binding.tvTitleSong.text = mSong.title
        binding.tvSingerSong.text = mSong.singer


        binding.imgPlayOrPause.setOnClickListener {
            if(isPlaying){
                sendActionToService(MyForegroundService.PAUSE)
            } else{
                sendActionToService(MyForegroundService.RESUME)
            }
        }

        binding.imgClose.setOnClickListener {
            sendActionToService(MyForegroundService.CLEAR)
        }
    }

    private fun sendActionToService(action: Int){
        val intent = Intent(this, MyForegroundService::class.java)
        intent.putExtra("action_music_service", action)
        startService(intent)
    }


    private fun setStatusButtonPlayOrPause(){
        if(isPlaying){
            binding.imgPlayOrPause.setImageResource(R.drawable.ic_pause)
        } else {
            binding.imgPlayOrPause.setImageResource(R.drawable.ic_play)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter("send_data_to_activity"))

        binding.btnStartService.setOnClickListener {
            clickStartService()
        }

        binding.btnStopService.setOnClickListener {
            clickStopService()
        }

    }

    private fun clickStartService() {
        val song = Song("Cruel Summer", "Taylor Swift", R.drawable.img_song, R.raw.mp3_song)
        val intent = Intent(this, MyForegroundService::class.java)
        val bundle = Bundle()
        bundle.putSerializable("object_song", song)
        intent.putExtras(bundle)

        // gọi onStartCommand()
        startService(intent)


    }

    private fun clickStopService() {
        val intent = Intent(this, MyForegroundService::class.java)
        stopService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

}