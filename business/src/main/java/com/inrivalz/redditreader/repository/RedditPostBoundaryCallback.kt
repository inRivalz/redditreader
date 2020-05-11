package com.inrivalz.redditreader.repository

import androidx.paging.PagedList
import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Boundary to get notified when the user reaches the end of the information provided by the DB.
 */
internal class RedditPostBoundaryCallback(
    private val redditBackend: RedditBackend,
    private val pagingRequestHelper: PagingRequestHelper,
    private val onPostFetched: (List<RedditPost>) -> Unit,
    private val pageSize: Int
) : PagedList.BoundaryCallback<RedditPost>() {

    val networkState: Observable<NetworkState> by lazy {
        val subject = BehaviorSubject.create<NetworkState>()
        pagingRequestHelper.addListener { report ->
            when {
                report.hasRunning() -> subject.onNext(NetworkState.Loading)
                report.hasError() -> subject.onNext(NetworkState.Failure)
                else -> subject.onNext(NetworkState.Success)
            }
        }
        subject.hide()
    }

    override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            redditBackend.getTopAfter(itemAtEnd.name, pageSize)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        onPostFetched(it)
                        callback.recordSuccess()
                    },
                    onError = { callback.recordFailure(it) }
                )
        }
    }
}
