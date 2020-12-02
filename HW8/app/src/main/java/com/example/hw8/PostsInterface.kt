package com.example.hw8

import androidx.room.*

@Dao
interface PostsInterface {
    @Delete
    fun delete(post : Post)

    @Insert
    fun insert(post : Post)

    @Query("SELECT * FROM Post")
    fun getAll() : List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts : List<Post>)

    @Query("DELETE FROM Post")
    fun deleteAll()

}