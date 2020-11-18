package com.example.hw7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.Serializable
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    data class Post(
        val title: String?,
        val body: String?
    ) : Serializable

    class PostViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(post: Post) {
            with(root) {
                title.text = post.title
                body.text = post.body
            }
        }
    }

    class PostAdapter(
        private val images: List<Post>,
        private val onClick: (Post) -> Unit
    ) : RecyclerView.Adapter<PostViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            var holder = PostViewHolder(
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

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
            holder.bind(images[position])
    }

//    lateinit var imagesList: List<Post>

    var imagesList: ArrayList<Post> = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Post post =
        imagesList.add(Post("Basketball", "Match was ....."))
        val postAdapter = PostAdapter(imagesList) {
//            val intent = Intent(this, ImageActivity::class.java)
//            try {
//                intent.putExtra("link", it.links?.download)
//                startActivity(intent)
//            } catch (e: Exception) {
//            }
        }
        val viewManager = LinearLayoutManager(this)

        myRecyclerView.apply {
            layoutManager = viewManager
//            adapter = photoAdapter
            adapter = PostAdapter(imagesList) {
                Toast.makeText(
                    this@MainActivity,
                    "Clicked on ${it.title}!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}