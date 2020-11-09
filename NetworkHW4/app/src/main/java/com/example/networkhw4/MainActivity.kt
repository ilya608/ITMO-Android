package com.example.networkhw4

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import org.xml.sax.Parser
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var imagesList: List<ImageClass>
    private val IMAGE_LIST_KEY = "IMAGE_LIST_KEY"

    companion object {
        val NO_DESCRIPTION = "No description"
    }

    data class Links(
        val download_location: String?,
        val download: String?
    ) : Serializable

    data class ImageClass(
        val id: String?,
        val links: Links?,
        val description: String?
    ) : Serializable

    data class ImageJson(
        val total: String?,
        val results: List<ImageClass>
    )

    class ContactViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(image: ImageClass) {
            with(root) {
                description.text = image.description ?: NO_DESCRIPTION
//                image.text = photo.link
//                name.text = photo.name
//                description.text = photo.description
            }
        }
    }

//    val photosList = (0..30).map {
//        Photo("First name #$it", "Last name #$it", "Name #$it")
//    }


    class PhotoAdapter(
        private val images: List<ImageClass>,
        private val onClick: (ImageClass) -> Unit
    ) : RecyclerView.Adapter<ContactViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            var holder = ContactViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            )
            holder.root.setOnClickListener {
                onClick(images[holder.adapterPosition])
            }

            return holder
        }

        override fun getItemCount() = images.size

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) =
            holder.bind(images[position])
    }

    class ImageDownloadAsyncTask : AsyncTask<String, Unit, String>() {
        private val accessKey: String = "kmxU-sqkbFyB8ZhoHNqFYPr1RtJL8lapPj6yqeXWvT0"
        private val link: String =
            "https://api.unsplash.com/search/photos?page=1&per_page=10&query=pizza&client_id=$accessKey"

        override fun doInBackground(vararg params: String?): String {
            Log.d("ASYNC_TASK", "doInBackground, Hello from ${Thread.currentThread().name}")
            val con = URL(link).openConnection() as HttpURLConnection
            val text: String
            try {
                con.connect()
                text = con.inputStream.use {
                    it.reader().use { reader -> reader.readText() }
                }

            } finally {
                con.disconnect()
            }

            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d("ASYNC_TASK", "onPostExecute, Hello from ${Thread.currentThread().name}")


            val a = 3
//            gson.fromJson<>(result)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val lst = ArrayList<ImageClass>()
        lst.addAll(imagesList)
        outState.putSerializable(IMAGE_LIST_KEY, lst)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewManager = LinearLayoutManager(this)
        var task : AsyncTask<String, Unit, String>? = null;
        if (savedInstanceState != null && savedInstanceState.containsKey(IMAGE_LIST_KEY) ) {
            imagesList = savedInstanceState.getSerializable(IMAGE_LIST_KEY) as ArrayList<ImageClass>
        } else {
            task = ImageDownloadAsyncTask().execute()
            val downloadRes = task?.get()
            val gson = Gson()
            val images: ImageJson = Gson().fromJson<ImageJson>(downloadRes, ImageJson::class.java)
            imagesList = images.results
        }
        val photoAdapter = PhotoAdapter(imagesList) {
            val intent = Intent(this, ImageActivity::class.java)
            try {
                intent.putExtra("link", it.links?.download)
                startActivity(intent)
            } catch (e: Exception) {
            }
        }
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = photoAdapter
//            adapter = PhotoAdapter(imagesList) {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Clicked on user $it!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }


    }
}