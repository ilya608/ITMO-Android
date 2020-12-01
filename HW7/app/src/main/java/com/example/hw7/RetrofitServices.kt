package com.example.hw7

import com.example.hw7.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitServices {
    @GET("posts")
    fun getListPosts() : Call<MutableList<Post>>

    @POST("posts")
    fun postData(@Body data: Post?): Call<Post?>
}