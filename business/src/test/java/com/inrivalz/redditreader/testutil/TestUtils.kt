package com.inrivalz.redditreader.testutil

import com.inrivalz.redditreader.business.entities.RedditPost

fun aRedditPost(): RedditPost = RedditPost(
    name = "name",
    title = "Post Title",
    author = "Author",
    created = 0,
    thumbnail = "thumbnail",
    comments = 0,
    subredit = ""
)
