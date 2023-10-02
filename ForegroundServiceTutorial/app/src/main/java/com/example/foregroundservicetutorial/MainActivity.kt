package com.example.foregroundservicetutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foregroundservicetutorial.databinding.ActivityMainBinding

private lateinit var binding : ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnStartService.setOnClickListener {
            clickStartService()
        }

        binding.btnStopService.setOnClickListener {
            clickStopService()
        }

    }

    private fun clickStartService() {
       val intent = Intent(this, MyForegroundService::class.java)
        intent.putExtra("key_data", binding.edtDataIntent.text.trim().toString())

        // gọi onStartCommand()
        startService(intent)


    }

    private fun clickStopService() {
        val intent = Intent(this, MyForegroundService::class.java)
        stopService(intent)
    }
}