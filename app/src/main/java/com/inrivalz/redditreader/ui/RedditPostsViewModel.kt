package com.inrivalz.redditreader.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.BaseViewModel
import com.inrivalz.redditreader.util.Logger
import com.inrivalz.redditreader.util.SingleLiveEvent
import io.reactivex.rxkotlin.subscribeBy

class RedditPostsViewModel(
    private val handle: SavedStateHandle,
    private val dispatcher: ItemSelectedDispatcher<RedditPost>,
    private val logger: Logger
) : BaseViewModel(), ItemSelectedDispatcher<RedditPost> by dispatcher {

    private val _uiEvents = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvents
    private var restoredFromState = false

    init {
        selectedItemStream.subscribeBy(
            onNext = { registerItemSelected(it) },
            onError = { logger.error(this@RedditPostsViewModel, exception = it) }
        ).autoClear()
        handle.get<RedditPost>(CURRENT_POST)?.let {
            restoredFromState = true
            onItemSelected(it)
        }
    }

    private fun registerItemSelected(post: RedditPost) {
        // We do not go to details if we restored an item selected from saved state
        if (!restoredFromState) {
            _uiEvents.value = UiEvent.ShowDetails
        }
        handle.set(CURRENT_POST, post)
    }

    sealed class UiEvent {
        object ShowDetails : UiEvent()
    }

    companion object {
        private const val CURRENT_POST = "CURRENT_POST"
    }
}
