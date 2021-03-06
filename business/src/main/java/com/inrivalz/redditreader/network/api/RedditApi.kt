package com.inrivalz.redditreader.network.api

import com.inrivalz.redditreader.network.data.JsonRedditListing
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RedditApi {

    @GET("/top.json")
    fun getTop(@Query("limit") limit: Int): Single<JsonRedditListing>

    @GET("/top.json")
    fun getTopAfter(
        @Query("after") after: String,
        @Query("limit") limit: Int
    ): Single <JsonRedditListing>
}
