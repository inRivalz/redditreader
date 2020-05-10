package com.inrivalz.redditreader.business.backend

import com.inrivalz.redditreader.business.entities.RedditPost
import io.reactivex.Single

/**
 * Call the backend and maps the entities from backend to the business ones.
 */
internal interface RedditBackend {

    fun getTop(limit: Int): Single<List<RedditPost>>

    fun getTopAfter(after: String, limit: Int): Single<List<RedditPost>>
}
