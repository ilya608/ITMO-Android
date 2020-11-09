package com.example.networkhw4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {
    lateinit var link: String
    val downloadService = MyIntentService()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_image)
        intent = intent
        link = intent.getStringExtra("link").toString()
        Toast.makeText(
            this,
            "Download...",
            Toast.LENGTH_SHORT
        ).show()
        val downloadIntent = Intent(this, MyIntentService::class.java)
        downloadIntent.putExtra("link", link)
        startService(downloadIntent)
        val intentFilter = IntentFilter(MyIntentService.KEY)
        val broadcastReceiver = object : BroadcastReceiver  () {
            override fun onReceive(context: Context?, intent: Intent?) {
                val a = 3
                imageWeb.setImageBitmap(MyIntentService.downloadMap[link])
            }
        }
        registerReceiver(broadcastReceiver, intentFilter)

//        MyIntentService.downloadMap[link]
    }
}