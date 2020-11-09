package com.example.networkhw4

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL


class MyIntentService : IntentService("MyIntentService") {

    companion object {
        val downloadMap: HashMap<String, Bitmap> = HashMap()
        val KEY = "afgjkljk324543nm,ngsfdgsdfkng&*@#434"
        val STATUS_OK = "ALL_IS_OK"
    }

    override fun onHandleIntent(intent: Intent?) {
        val link = intent?.getStringExtra("link")

        val con = URL(link).openConnection() as HttpURLConnection
        val text: String
        lateinit var bitmap : Bitmap
        try {
            con.connect()
            bitmap = BitmapFactory.decodeStream(con.inputStream)
            text = con.inputStream.use {
                it.reader().use { reader -> reader.readText() }
            }
            downloadMap.put(link.toString(), bitmap)
            val intent = Intent(KEY).putExtra("STATUS", STATUS_OK)
            sendBroadcast(intent)

        } finally {
            con.disconnect()
        }
        val a = 3
    }

//    override fun onDestroy() {
//        super.onDestroy()
//    }
}
