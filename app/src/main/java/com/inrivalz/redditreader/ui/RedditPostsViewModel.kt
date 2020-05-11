package com.inrivalz.redditreader.ui

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.BaseViewModel
import com.inrivalz.redditreader.util.Logger
import com.inrivalz.redditreader.util.SingleLiveEvent
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.parcel.Parcelize

class RedditPostsViewModel(
    private val handle: SavedStateHandle,
    private val dispatcher: ItemSelectedDispatcher<RedditPost>,
    private val logger: Logger
) : BaseViewModel(), ItemSelectedDispatcher<RedditPost> by dispatcher {

    private val _uiState = MutableLiveData<UiState>().apply { value = UiState.ShowMaster }
    val uiState: LiveData<UiState> = _uiState

    private val _uiEvents = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvents

    init {
        selectedItemStream.subscribeBy(
            onNext = { registerItemSelected(it) },
            onError = { logger.error(this@RedditPostsViewModel, exception = it) }
        ).autoClear()
        handle.get<UiState>(CURRENT_STATE)?.let { state ->
            if (state is UiState.ShowDetails) {
                handle.get<RedditPost>(CURRENT_POST)?.let { onItemSelected(it) }
            }
        }
    }

    private fun registerItemSelected(post: RedditPost) {
        _uiState.value = UiState.ShowDetails
        handle.set(CURRENT_STATE, UiState.ShowDetails)
        handle.set(CURRENT_POST, post)
    }

    fun onBackPressed() {
        if (uiState.value is UiState.ShowMaster) {
            _uiEvents.value = UiEvent.ExitApplication
        } else {
            _uiState.value = UiState.ShowMaster
            handle.set(CURRENT_STATE, UiState.ShowMaster)
        }
    }


    sealed class UiState : Parcelable {
        @Parcelize
        object ShowMaster : UiState()
        @Parcelize
        object ShowDetails : UiState()
    }

    sealed class UiEvent {
        object ExitApplication : UiEvent()
    }

    companion object {
        private const val CURRENT_POST = "CURRENT_POST"
        private const val CURRENT_STATE = "CURRENT_STATE"
    }
}
