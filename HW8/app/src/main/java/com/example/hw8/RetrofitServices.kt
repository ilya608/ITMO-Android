package com.example.hw8

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