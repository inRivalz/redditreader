package com.inrivalz.redditreader.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.BaseViewModel
import com.inrivalz.redditreader.util.Logger
import com.inrivalz.redditreader.util.SingleLiveEvent
import io.reactivex.rxkotlin.subscribeBy

class RedditPostsViewModel(
    dispatcher: ItemSelectedDispatcher<RedditPost>,
    private val logger: Logger
) : BaseViewModel(), ItemSelectedDispatcher<RedditPost> by dispatcher {

    private val _uiState = MutableLiveData<UiState>().apply { value = UiState.ShowMaster }
    val uiState: LiveData<UiState> = _uiState

    private val _uiEvents = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvents

    init {
        selectedItemStream.subscribeBy(
            onNext = { onItemSelected() },
            onError = { logger.error(this@RedditPostsViewModel, exception = it) }
        ).autoClear()
    }

    private fun onItemSelected() {
        _uiState.value = UiState.ShowDetails
    }

    fun onBackPressed() {
        if (uiState.value is UiState.ShowMaster) {
            _uiEvents.value = UiEvent.ExitApplication
        } else {
            _uiState.value = UiState.ShowMaster
        }
    }

    sealed class UiState {
        object ShowMaster : UiState()
        object ShowDetails : UiState()
    }

    sealed class UiEvent {
        object ExitApplication : UiEvent()
    }
}
