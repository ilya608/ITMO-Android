package com.example.hw7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.io.Serializable

class MainActivity : AppCompatActivity() {

//    data class UserEntity(@field:Json(name = "id") val id: String,
//                          @field:Json(name = "name") val name: String)

    private var POST_KEY = "POST_KEY"
    data class Post(
        @SerializedName("title")
        val title: String?,
        @SerializedName("body")
        val body: String?,
        @SerializedName("userId")
        val userId: Int?,
        @SerializedName("id")
        val id: Int?
    ) : Serializable


    class PostAdapter(
        private var posts: MutableList<Post>,
        private val onClick: (Post) -> Unit
    ) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            var holder = PostViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            )
            holder.root.setOnClickListener {
                onClick(posts[holder.adapterPosition])
            }

            holder.root.delete.setOnClickListener()  {
                posts.removeAt(holder.adapterPosition)
                System.out.println("New size " + posts.size)
                notifyDataSetChanged()

            }
            return holder
        }

        override fun getItemCount() = posts.size

        fun getPosts() : List<Post> {
            return posts
        }

        fun setPostsList(newPosts: MutableList<Post>) {
            posts = newPosts
            notifyDataSetChanged()
        }


        override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
            holder.bind(posts[position])

        class PostViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            fun bind(post: Post) {
                with(root) {
                    title.text = post.title
                    body.text = post.body
                }
            }
        }

    }

//    lateinit var postsList: List<Post>

    var postsList: ArrayList<Post> = ArrayList<Post>()
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(POST_KEY, postsList)
        val a = 3
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        postsList = savedInstanceState.get(POST_KEY) as ArrayList<Post>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add_button.setOnClickListener() {
            Toast.makeText(
                this@MainActivity,
                "Post published",
                Toast.LENGTH_SHORT
            ).show()
        }
        val BASE_URL = "https://jsonplaceholder.typicode.com/"
        val viewManager = LinearLayoutManager(this)
        val postAdapter = PostAdapter(postsList) {}
        if (savedInstanceState != null) {
            postsList = savedInstanceState.get(POST_KEY) as ArrayList<Post>
            progress.visibility = View.INVISIBLE
            postAdapter.setPostsList(postsList)
        } else {

            val retrofit: Retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitService: RetrofitServices = retrofit.create(RetrofitServices::class.java)

            val list = retrofitService.getListPosts()

            list.enqueue(object : Callback<MutableList<Post>> {
                override fun onFailure(call: Call<MutableList<Post>>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                }

                override fun onResponse(
                    call: Call<MutableList<Post>>?,
                    response: Response<MutableList<Post>>?
                ) {
                    if (response != null) {
                        response.body()?.let {
                            progress.visibility = View.INVISIBLE
                            postAdapter.setPostsList(it)
                            postsList = postAdapter.getPosts() as ArrayList<Post>
                        }
//
                    }
                }
            })
        }
        myRecyclerView.apply {
            layoutManager = viewManager
//            adapter = photoAdapter
            adapter = postAdapter
        }
    }
}