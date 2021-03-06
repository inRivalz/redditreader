package com.inrivalz.redditreader.business.backend.map

import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.data.JsonRedditPost

internal fun JsonRedditPost.toRedditPost(): RedditPost {
    return RedditPost(
        name = name,
        title = title,
        author = author,
        created = created,
        thumbnail = thumbnail,
        comments = comments,
        subreddit = subreddit
    )
}
