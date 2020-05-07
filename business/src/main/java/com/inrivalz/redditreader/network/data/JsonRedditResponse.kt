package com.inrivalz.redditreader.network.data

import kotlinx.serialization.SerialName

data class JsonRedditResponse(
    @SerialName("data") val data: JsonRedditPostsData
)

data class JsonRedditPostsData(
    @SerialName("children") val children: List<JsonRedditPostResponse>,
    @SerialName("after") val after: String
)

data class JsonRedditPostResponse(
    @SerialName("data") val post: JsonRedditPost
)

data class JsonRedditPost(
    @SerialName("name") val name: String,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("created") val long: String,
    @SerialName("thumbnail") val thumbnail: String
)