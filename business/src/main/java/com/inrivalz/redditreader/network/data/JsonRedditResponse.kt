package com.inrivalz.redditreader.network.data

import kotlinx.serialization.SerialName

// For now I'll leave all the classes here, but we can move them outside if needed
internal data class JsonRedditResponse(
    @SerialName("data") val data: JsonRedditPostsData
)

internal data class JsonRedditPostsData(
    @SerialName("children") val children: List<JsonRedditPostResponse>,
    @SerialName("after") val after: String
)

internal data class JsonRedditPostResponse(
    @SerialName("data") val post: JsonRedditPost
)

internal data class JsonRedditPost(
    @SerialName("name") val name: String,
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("created") val long: String,
    @SerialName("thumbnail") val thumbnail: String
)