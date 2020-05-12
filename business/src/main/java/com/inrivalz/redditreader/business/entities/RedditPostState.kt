package com.inrivalz.redditreader.business.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts_state")
data class RedditPostState(
    @PrimaryKey
    val name: String,
    val dismissed: Boolean,
    val read: Boolean
)
