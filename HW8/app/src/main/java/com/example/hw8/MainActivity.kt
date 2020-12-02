package com.example.hw8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private var POST_KEY = "POST_KEY"
    val BASE_URL = "https://jsonplaceholder.typicode.com/"

    companion object {
        lateinit var db: AppDatabase
        var retrofitIsInit = false
        lateinit var retrofitService: RetrofitServices
        lateinit var postAdapter : PostAdapter
        var maxKey: Number = 101
        var postsList: ArrayList<Post> = ArrayList<Post>()
    }

    class PostAdapter(
        private var posts: MutableList<
                Post>,
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

            holder.root.delete.setOnClickListener() {
                db.postDao()?.delete(posts[holder.adapterPosition])
                posts.removeAt(holder.adapterPosition)
                System.out.println("New size " + posts.size)
                notifyDataSetChanged()


            }
            return holder
        }

        override fun getItemCount() = posts.size

        fun getPosts(): List<Post> {
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(POST_KEY, postsList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        postsList = savedInstanceState.get(POST_KEY) as ArrayList<Post>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (!retrofitIsInit) {
            val retrofit: Retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofitService = retrofit.create(RetrofitServices::class.java)
            retrofitIsInit = true
        }
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .build()

        add_button.setOnClickListener() {
            val intent = Intent(this, SendPost::class.java)
            startActivity(intent)
        }

        val viewManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postsList) {}

        refresh_button.setOnClickListener() {
            progress.visibility = View.VISIBLE
            db.postDao()?.deleteAll()
            val list = retrofitService.getListPosts()
            list.enqueue(object : Callback<MutableList<Post>> {
                override fun onFailure(call: Call<MutableList<Post>>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                    progress.visibility = View.INVISIBLE
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
                            maxKey = postsList.size + 1

                            for (i in 99 downTo 10) {
                                postsList.removeAt(i)
                            }
                            db.postDao()?.insertAll(postsList)
                        }
                    }
                }
            })
        }
        if (savedInstanceState != null) {
            postsList = savedInstanceState.get(POST_KEY) as ArrayList<Post>
            progress.visibility = View.INVISIBLE
            postAdapter.setPostsList(postsList)
        } else {
            postsList = db.postDao()?.getAll() as ArrayList<Post>
            postAdapter.setPostsList(postsList)
        }
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = postAdapter
        }
    }
}
