package com.example.hw8

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Post(

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?,

    @SerializedName("body")
    @ColumnInfo(name = "body")
    val body: String?,

    @SerializedName("userId")
    @ColumnInfo(name = "userId")
    val userId: Int?,

    @SerializedName("id")
    @PrimaryKey val id: Int?
) : Serializable