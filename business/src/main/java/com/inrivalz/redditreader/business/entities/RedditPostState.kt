package com.inrivalz.redditreader.business.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "posts_state")
data class RedditPostState(
    @PrimaryKey
    @SerializedName("name")
    val name: String,
    @SerializedName("dismissed")
    val dismissed: Boolean = false,
    @SerializedName("read")
    val read: Boolean = false
)