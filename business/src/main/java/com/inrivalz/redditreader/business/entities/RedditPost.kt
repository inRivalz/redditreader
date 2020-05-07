package com.inrivalz.redditreader.business.entities

internal data class RedditPost(
    val name: String,
    val title: String,
    val author: String,
    val long: String,
    val thumbnail: String,
    val read: Boolean = false
)