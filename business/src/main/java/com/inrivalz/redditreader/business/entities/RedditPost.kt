package com.inrivalz.redditreader.business.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "posts")
data class RedditPost(
    @PrimaryKey
    val name: String,
    val title: String,
    val author: String,
    val created: Long,
    val thumbnail: String?,
    val comments: Int = 0,
    val subreddit: String,
    val read: Boolean = false,
    val position: Int = 0
) : Parcelable
