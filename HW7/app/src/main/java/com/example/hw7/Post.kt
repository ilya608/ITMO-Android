package com.example.hw7

import com.google.gson.annotations.SerializedName
import java.io.Serializable

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