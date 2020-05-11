package com.inrivalz.redditreader.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import com.inrivalz.redditreader.util.BaseViewModel
import com.inrivalz.redditreader.util.Logger
import com.inrivalz.redditreader.util.SingleLiveEvent
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import io.ktor.util.cio.writeChannel
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.io.copyAndClose
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class RedditPostDetailsViewModel(
    itemSelectedDispatcher: ItemSelectedDispatcher<RedditPost>,
    private val logger: Logger,
    private val httpClient: HttpClient
) : BaseViewModel() {

    private val _postState = MutableLiveData<RedditPost>()
    val postState: LiveData<RedditPost> = _postState

    private val _uiEvent = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvent

    init {
        itemSelectedDispatcher.selectedItemStream
            .subscribeBy(
                onNext = { _postState.postValue(it) },
                onError = { logger.error(this@RedditPostDetailsViewModel, exception = it) }
            ).autoClear()
    }

    fun openImage() {
        _postState.value?.thumbnail?.let {
            _uiEvent.value = UiEvent.OpenBrowser(it)
        }
    }

    fun openImage(file: File, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.downloadFile(file, url) { saved ->
                withContext(Dispatchers.Main) {
                    if (saved) {
                        _uiEvent.value = UiEvent.OpenFile(file)
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class OpenBrowser(val url: String) : UiEvent()
        data class OpenFile(val file: File) : UiEvent()
    }
}

suspend fun HttpClient.downloadFile(file: File, url: String, callback: suspend (boolean: Boolean) -> Unit) {
    val call = call {
        url(url)
        method = HttpMethod.Get
    }
    if (!call.response.status.isSuccess()) {
        callback(false)
    }
    call.response.content.copyAndClose(file.writeChannel())
    return callback(true)
}