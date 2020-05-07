package com.inrivalz.redditreader.network.data

import kotlinx.serialization.SerialName

// For now I'll leave all the classes here, but we can move them outside if needed
internal data class JsonRedditListing(
    @SerialName("data") val data: JsonRedditListingData
)

internal data class JsonRedditListingData(
    @SerialName("children") val children: List<JsonRedditListingChild>,
    @SerialName("after") val after: String
)

internal data class JsonRedditListingChild(
    @SerialName("data") val post: JsonRedditPost
)

internal data class JsonRedditPost(
    @SerialName("title") val title: String,
    @SerialName("author") val author: String,
    @SerialName("created") val created: Long,
    @SerialName("thumbnail") val thumbnail: String
)