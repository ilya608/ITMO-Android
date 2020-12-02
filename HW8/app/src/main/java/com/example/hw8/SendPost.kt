package com.example.hw8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_send_post.*

class SendPost : AppCompatActivity() {
    companion object;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_post)

        send_button.setOnClickListener {
            val title: String = title_window.text.toString()
            val text: String = text_window.text.toString()

            val post = Post(title, text, 1, MainActivity.maxKey.toInt() + 1)
            MainActivity.maxKey = MainActivity.maxKey.toInt() + 1
            try {
                MainActivity.db.postDao()?.insert(post)
                MainActivity.postsList.add(post)
                MainActivity.postAdapter.setPostsList(MainActivity.postsList)
                Toast.makeText(
                    applicationContext,
                    "Отправлено", Toast.LENGTH_SHORT
                ).show()
                finish()
            } catch (e : Exception) {
                println(e.localizedMessage)
                Toast.makeText(
                    applicationContext,
                    "Что-то пошло не так", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}