package com.example.hw7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_send_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendPost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_post)

        send_button.setOnClickListener() {
            val title: String = title_window.toString()
            val text: String = text_window.toString()
            System.out.println(title)
            val post = Post(title, text, 1, 1)
            val res : Call<Post?> = MainActivity.retrofitService.postData(post)
            res?.enqueue(object : Callback<Post?> {
                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Ошибка", Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

                override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                    Toast.makeText(
                        applicationContext,
                        "Отправлено ", Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            })
            Toast.makeText(
                applicationContext,
                "Отправлено", Toast.LENGTH_SHORT
            ).show()
        }
    }
}