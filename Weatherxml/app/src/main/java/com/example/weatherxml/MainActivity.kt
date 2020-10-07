package com.example.weatherxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val switcher = findViewById<Switch>(R.id.theme);

        switcher.setOnCheckedChangeListener({ _ , isChecked ->
            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            System.out.println(message);
        })
    }


}