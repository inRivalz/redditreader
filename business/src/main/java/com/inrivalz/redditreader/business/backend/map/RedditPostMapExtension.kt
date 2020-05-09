package com.inrivalz.redditreader.business.map

import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.data.JsonRedditPost

internal fun JsonRedditPost.toRedditPost(): RedditPost {
    return RedditPost(
        title = title,
        author = author,
        created = created,
        thumbnail = thumbnail
    )
}