package com.inrivalz.redditreader.business.backend

import com.inrivalz.redditreader.business.backend.map.toRedditPost
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.api.RedditApi
import io.reactivex.Single

internal class RedditBackendImpl(
    private val redditApi: RedditApi
) : RedditBackend {

    override fun getTop(limit: Int): Single<List<RedditPost>> {
        return redditApi.getTop(limit).map { listing ->
            listing.data.children.map { child -> child.post.toRedditPost() }
        }
    }

    override fun getTopAfter(after: String, limit: Int): Single<List<RedditPost>> {
        return redditApi.getTopAfter(after, limit).map { listing ->
            listing.data.children.map { child -> child.post.toRedditPost() }
        }
    }
}
