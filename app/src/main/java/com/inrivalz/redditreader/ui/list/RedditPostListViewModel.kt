package com.inrivalz.redditreader.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.repository.RedditPostsRepository
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import io.reactivex.rxkotlin.subscribeBy

class RedditPostListViewModel(
    private val itemSelectedDispatcher: ItemSelectedDispatcher<RedditPost>,
    private val redditPostsRepository: RedditPostsRepository
) : ViewModel() {

    private var _listState = MutableLiveData<PagedList<RedditPost>>()
    val listState: LiveData<PagedList<RedditPost>> = _listState

    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    init {
        redditPostsRepository.getTopPosts().pagedData
            .subscribeBy(onNext = { _listState.postValue(it) })
        // TODO Error
    }

    fun refresh() {
        redditPostsRepository.refreshPosts()
        _networkState.value = NetworkState.Success
    }

    fun onItemSelected(post: RedditPost) {
        itemSelectedDispatcher.onItemSelected(post)
    }
}
