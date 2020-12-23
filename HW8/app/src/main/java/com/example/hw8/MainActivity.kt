package com.example.hw8

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {

    private var POST_KEY = "POST_KEY"
    val BASE_URL = "https://jsonplaceholder.typicode.com/"

    companion object {
        var myAsyncTasks: ArrayList<AsyncTask<Void, Void, Void>> = ArrayList()
        lateinit var db: AppDatabase
        var retrofitIsInit = false
        lateinit var retrofitService: RetrofitServices
        lateinit var postAdapter: PostAdapter
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
                val task =
                    RunAsync({
                        db.postDao()?.delete(posts[holder.adapterPosition])
                    }, {
                        posts.removeAt(holder.adapterPosition)
                        System.out.println("New size " + posts.size)
                        notifyDataSetChanged()
                    })
                myAsyncTasks.add(task)
                task.execute()


            }
            return holder
        }

        override fun getItemCount() = posts.size

        fun getPosts(): List<Post> {
            return posts
        }

        fun setPostsList(newPosts: ArrayList<Post>) {
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

    class RunAsync(val handler: () -> Unit, val postExecuteHandler: () -> Unit) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            postExecuteHandler()
        }
    }

    class RetrofitCall(activity: Activity) : AsyncTask<Void, Void, ArrayList<Post>>() {
        private var weakReference: WeakReference<Activity> = WeakReference(activity)
        private var errorMessage: String = ""
        override fun onPreExecute() {
            weakReference.get()?.progress?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): ArrayList<Post>? {
            db.postDao()?.deleteAll()
            try {
                val list = retrofitService.getListPosts().execute()
//                for (i in 99 downTo 10) {
//                    postsList.removeAt(i)
//                }
                val posts: ArrayList<Post> = list.body() as ArrayList<Post>
                db.postDao()?.insertAll(posts)
                return posts
            } catch (e: java.lang.Exception) {
                errorMessage = "Internet error"
            }
            return null

        }

        override fun onPostExecute(result: ArrayList<Post>?) {
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(weakReference.get(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            result?.let { postAdapter.setPostsList(it) }
            postsList = postAdapter.getPosts() as ArrayList<Post>
            maxKey = postsList.size + 1
            weakReference.get()?.progress?.visibility = View.INVISIBLE

        }

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
        val viewManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postsList) {}
        if (savedInstanceState != null) {
            postsList = savedInstanceState.get(POST_KEY) as ArrayList<Post>
            progress.visibility = View.INVISIBLE
            postAdapter.setPostsList(postsList)
        } else {
            val task =
            RunAsync({
                postsList = db.postDao()?.getAll() as ArrayList<Post>
            }, {
                postAdapter.setPostsList(postsList)
            })
            myAsyncTasks.add(task)
            task.execute()
        }
        add_button.setOnClickListener() {
            val intent = Intent(this, SendPost::class.java)
            startActivity(intent)
        }

        refresh_button.setOnClickListener() {
            RetrofitCall(this).execute()
        }
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = postAdapter
        }
    }

    override fun onDestroy() {
        for (task in myAsyncTasks) {
            if (task.status == AsyncTask.Status.RUNNING) {
                task.cancel(true)
            }
        }
        myAsyncTasks.clear()
        super.onDestroy()
    }
}