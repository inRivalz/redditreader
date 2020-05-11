package com.inrivalz.redditreader.network.data

import com.google.gson.annotations.SerializedName

// For now I'll leave all the classes here, but we can move them outside if needed
internal data class JsonRedditListing(
    @SerializedName("data") val data: JsonRedditListingData
)

internal data class JsonRedditListingData(
    @SerializedName("children") val children: List<JsonRedditListingChild>,
    @SerializedName("after") val after: String
)

internal data class JsonRedditListingChild(
    @SerializedName("data") val post: JsonRedditPost
)

internal data class JsonRedditPost(
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("created") val created: Long,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("num_comments") val comments: Int
)
