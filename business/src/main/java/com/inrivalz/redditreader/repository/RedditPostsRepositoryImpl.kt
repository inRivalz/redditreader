package com.inrivalz.redditreader.repository

import androidx.paging.toObservable
import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.db.RedditPostsDao
import io.reactivex.Completable
import java.util.concurrent.Executor

internal class RedditPostsRepositoryImpl(
    private val redditPostBoundaryCallbackFactory: RedditPostBoundaryCallbackFactory,
    private val redditPostsDao: RedditPostsDao,
    private val redditBackend: RedditBackend,
    private val ioExecutor: Executor
) : RedditPostsRepository {

    override fun getTopPosts(pageSize: Int): PagedRepositoryResponse {
        val helper = PagingRequestHelper(ioExecutor)
        val boundaryCallback = redditPostBoundaryCallbackFactory.create(
            helper = helper,
            onPostFetched = ::insertPostsIntoDb,
            pageSize = pageSize
        )

        val pagedData = redditPostsDao.getUnreadPosts()
            .toObservable(pageSize, boundaryCallback = boundaryCallback)

        return PagedRepositoryResponse(
            pagedData = pagedData,
            networkState = boundaryCallback.networkState,
            onRetry = { helper.retryAllFailed() }
        )
    }

    override fun refreshPosts(pageSize: Int): Completable {
        return redditBackend.getTop(pageSize)
            .doOnSuccess {
                ioExecutor.execute { redditPostsDao.cleanAnInsert(it) }
            }.toCompletable()
    }

    private fun insertPostsIntoDb(posts: List<RedditPost>) {
        ioExecutor.execute {
            redditPostsDao.insertSorted(posts)
        }
    }
}
