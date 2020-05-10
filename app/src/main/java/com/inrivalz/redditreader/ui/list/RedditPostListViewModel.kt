package com.inrivalz.redditreader.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher

class RedditPostListViewModel(
    private val itemSelectedDispatcher: ItemSelectedDispatcher<RedditPost>
) : ViewModel() {

    private var _listState = MutableLiveData<List<RedditPost>>()
    val listState: LiveData<List<RedditPost>> = _listState

    private var _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    init {
        _listState.value = listOf(
            RedditPost(title = "Post Title", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title2", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title3", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title4", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title5", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title6", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title7", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title8", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title9", author = "Author", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title10", author = "Author", created = 0, thumbnail = "thumbnail")
        )
    }

    fun refresh() {
        _networkState.value = NetworkState.Success
    }
}
