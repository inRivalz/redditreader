package com.inrivalz.redditreader.repository

import androidx.paging.PagedList
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import io.reactivex.Completable
import io.reactivex.Observable

interface RedditPostsRepository {
    fun getTopPosts(pageSize: Int): PagedRepositoryResponse
    fun refreshPosts(pageSize: Int = DEFAULT_PAGE_SIZE): Completable
    fun markPostAsDismissed(post: RedditPost)
    fun markPostAsRead(post: RedditPost)
    fun dismissAll()
    fun clearAllDismissed()

    companion object {
        private const val DEFAULT_PAGE_SIZE = 30
    }
}

data class PagedRepositoryResponse(
    val pagedData: Observable<PagedList<RedditPost>>,
    val networkState: Observable<NetworkState>,
    val onRetry: () -> Unit
)
