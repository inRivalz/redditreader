package com.inrivalz.redditreader.repository

import androidx.paging.PagedList
import androidx.paging.toObservable
import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.db.RedditDatabase
import com.inrivalz.redditreader.db.RedditPostsDao
import com.inrivalz.redditreader.network.NetworkState
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

interface RedditPostsRepository {

    fun getTopPosts(): PagedRepositoryResponse
    fun refreshPosts(): Observable<NetworkState>

}

internal class RedditPostsRepositoryImpl(
    private val redditDatabase: RedditDatabase,
    private val redditPostsDao: RedditPostsDao,
    private val redditBackend: RedditBackend
) : RedditPostsRepository {

    override fun getTopPosts(): PagedRepositoryResponse {
        val helper = PagingRequestHelper(Executors.newSingleThreadExecutor())
        val boundaryCallback = RedditPostBoundaryCallback(
            redditBackend,
            helper,
            ::insertPostsIntoDb,
            20
        )

        val pagedData = redditPostsDao.getUnreadPosts().toObservable(20, boundaryCallback = boundaryCallback)

        return PagedRepositoryResponse(
            pagedData = pagedData,
            networkState = boundaryCallback.networkState,
            refreshState = Observable.just(NetworkState.Success),
            onRetry = { helper.retryAllFailed() },
            onRefresh = { refreshPosts() }
        )
    }

    override fun refreshPosts(): Observable<NetworkState> {
        redditBackend.getTop(20)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
            onSuccess = {
                redditDatabase.runInTransaction {
                    redditPostsDao.deleteUnreadPosts()
                    redditPostsDao.insert(it)
                }
            }
        // TODO On error
        )
        return Observable.just(NetworkState.Success)
    }


    private fun insertPostsIntoDb(posts: List<RedditPost>) {
        redditPostsDao.insertSorted(posts)
    }

}

data class PagedRepositoryResponse(
    val pagedData: Observable<PagedList<RedditPost>>,
    val networkState: Observable<NetworkState>,
    val refreshState: Observable<NetworkState>,    // TODO: Esto sacalo y movelo al usecase, no hace falta aca
    val onRetry: () -> Unit,
    val onRefresh: () -> Unit // Este tambien
)