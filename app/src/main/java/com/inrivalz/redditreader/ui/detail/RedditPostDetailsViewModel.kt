package com.inrivalz.redditreader.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import io.reactivex.rxkotlin.subscribeBy

class RedditPostDetailsViewModel(
    itemSelectedDispatcher: ItemSelectedDispatcher<RedditPost>
) : ViewModel() {

    private val _postState = MutableLiveData<RedditPost>()
    val postState: LiveData<RedditPost> = _postState

    init {
        itemSelectedDispatcher.selectedItemStream
            .subscribeBy(
                onNext = { _postState.postValue(it) },
                onError = { Log.e(this.toString(), it.message, it) }
            )
    }

}