package com.inrivalz.redditreader.repository

import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.business.entities.RedditPost

internal class RedditPostBoundaryCallbackFactory(
    private val redditBackend: RedditBackend
) {
    fun create(helper: PagingRequestHelper, onPostFetched: (List<RedditPost>) -> Unit, pageSize: Int) =
        RedditPostBoundaryCallback(redditBackend, helper, onPostFetched, pageSize)
}