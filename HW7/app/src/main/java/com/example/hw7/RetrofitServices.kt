package com.example.hw7

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitServices {
    @GET("posts")
    fun getListPosts() : Call<MutableList<MainActivity.Post>>
}