package com.inrivalz.redditreader.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.repository.RedditPostsRepository
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import com.inrivalz.redditreader.util.BaseViewModel
import com.inrivalz.redditreader.util.Logger
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RedditPostListViewModel(
    private val itemSelectedDispatcher: ItemSelectedDispatcher<RedditPost>,
    private val redditPostsRepository: RedditPostsRepository,
    private val logger: Logger
) : BaseViewModel() {

    private var _listState = MutableLiveData<PagedList<RedditPost>>()
    val listState: LiveData<PagedList<RedditPost>> = _listState

    private var _pagedNetworkState = MutableLiveData<NetworkState>()
    val pagedNetworkState: LiveData<NetworkState> = _pagedNetworkState

    private var _refreshState = MutableLiveData<NetworkState>()
    val refreshState: LiveData<NetworkState> = _refreshState

    private var retryPagedNetworkRequest: () -> Unit = {}

    init {
        val pagedResponse = redditPostsRepository.getTopPosts(PAGE_SIZE)
        retryPagedNetworkRequest = pagedResponse.onRetry
        pagedResponse.pagedData
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { _listState.postValue(it) },
                onError = { logger.error(this@RedditPostListViewModel, exception = it) }
            ).autoClear()
        observePagedNetworkState(pagedResponse.networkState)
    }

    private fun observePagedNetworkState(networkState: Observable<NetworkState>) {
        networkState
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { _pagedNetworkState.postValue(it) },
                onError = { logger.error(this@RedditPostListViewModel, exception = it) }
            ).autoClear()
    }

    fun refresh() {
        _refreshState.value = NetworkState.Loading
        redditPostsRepository.refreshPosts()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { _refreshState.postValue(NetworkState.Success) },
                onError = { _refreshState.postValue(NetworkState.Failure) }
            ).autoClear()
    }

    fun onItemSelected(post: RedditPost) {
        itemSelectedDispatcher.onItemSelected(post)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
