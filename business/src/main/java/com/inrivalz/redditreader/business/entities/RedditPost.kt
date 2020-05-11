package com.inrivalz.redditreader.business.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "posts")
data class RedditPost(
    @PrimaryKey
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("created")
    val created: Long,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("comments")
    val comments: Int = 0,
    @SerializedName("subredit")
    val subredit: String,
    @SerializedName("read")
    val read: Boolean,
    @SerializedName("position")
    val position: Int = 0
)
