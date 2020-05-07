package com.inrivalz.redditreader.business.entities

internal data class RedditPost(
    val title: String,
    val author: String,
    val created: Long,
    val thumbnail: String,
    val read: Boolean = false
)