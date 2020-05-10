package com.inrivalz.redditreader.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import com.inrivalz.redditreader.util.BaseViewModel
import com.inrivalz.redditreader.util.Logger
import io.reactivex.rxkotlin.subscribeBy

class RedditPostDetailsViewModel(
    itemSelectedDispatcher: ItemSelectedDispatcher<RedditPost>,
    private val logger: Logger
) : BaseViewModel() {

    private val _postState = MutableLiveData<RedditPost>()
    val postState: LiveData<RedditPost> = _postState

    init {
        itemSelectedDispatcher.selectedItemStream
            .subscribeBy(
                onNext = { _postState.postValue(it) },
                onError = { logger.error(this@RedditPostDetailsViewModel, exception = it) }
            ).autoClear()
    }
}
