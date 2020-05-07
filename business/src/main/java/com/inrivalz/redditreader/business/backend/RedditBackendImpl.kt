package com.inrivalz.redditreader.business.backend

import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.api.RedditApi
import io.reactivex.Single

internal class RedditBackendImpl(
    private val redditApi: RedditApi
) : RedditBackend {

    override fun getTop(limit: Int): Single<List<RedditPost>> {
        TODO("Not yet implemented")
    }

    override fun getTopAfter(after: String, limit: Int): Single<List<RedditPost>> {
        TODO("Not yet implemented")
    }
}