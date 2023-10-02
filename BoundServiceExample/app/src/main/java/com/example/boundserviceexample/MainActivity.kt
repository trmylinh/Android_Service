package com.example.boundserviceexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.boundserviceexample.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var mMusicBoundService: MusicBoundService
    private var isServiceConnected = false

    private val mServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val myBinder: MusicBoundService.MyBinder = iBinder as MusicBoundService.MyBinder
            mMusicBoundService = myBinder.getMusicBoundService()
            mMusicBoundService.startMusic()
            isServiceConnected = true
        }


        // khong duoc goi khi ham unbindService() duoc goi
        // chi duoc goi khi nao service chet dot ngot, khong du tai nguyen .....
        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceConnected = false
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startBoundService.setOnClickListener {
            onClickStartService()
        }

        binding.stopBoundService.setOnClickListener {
            onClickStopService()
        }
    }

    private fun onClickStopService() {
        if(isServiceConnected){
            unbindService(mServiceConnection)
            isServiceConnected = false
        }

    }

    private fun onClickStartService() {
        val intent = Intent(this, MusicBoundService::class.java)

        //rang buoc voi bound service
        // phai check xem da connect duoc voi service chua
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE)
    }
}