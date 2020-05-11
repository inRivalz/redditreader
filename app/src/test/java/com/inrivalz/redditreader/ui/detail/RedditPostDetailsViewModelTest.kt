package com.inrivalz.redditreader.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.testutil.aRedditPost
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import com.inrivalz.redditreader.util.Logger
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.client.HttpClient
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class RedditPostDetailsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val itemSelectedDispatcher = mock<ItemSelectedDispatcher<RedditPost>>()
    private val logger = mock<Logger>()
    private val httpClient = mock<HttpClient>()

    private lateinit var redditPostDetailsViewModel: RedditPostDetailsViewModel

    private val postStateObserver = mock<Observer<RedditPost>>()

    @Test
    fun `Should emit reddit post from dispatcher on initialization`() {
        val redditPost = aRedditPost()
        whenever(itemSelectedDispatcher.selectedItemStream).doReturn(Observable.just(redditPost))

        viewModelIsInitialized()

        verify(itemSelectedDispatcher).selectedItemStream
        verify(postStateObserver).onChanged(check {
            assertThat(it).isEqualTo(redditPost)
        })
    }

    @Test
    fun `Should not emit anything if no post is returned by dispatcher`() {
        whenever(itemSelectedDispatcher.selectedItemStream).doReturn(Observable.never())

        viewModelIsInitialized()

        verify(itemSelectedDispatcher).selectedItemStream
        verify(postStateObserver, never()).onChanged(any())
    }

    @Test
    fun `Should log error and not emit anything if the observable returns error`() {
        val exception = Exception("error")
        whenever(itemSelectedDispatcher.selectedItemStream).doReturn(Observable.error(exception))

        viewModelIsInitialized()

        verify(itemSelectedDispatcher).selectedItemStream
        verify(postStateObserver, never()).onChanged(any())
        verify(logger).error(redditPostDetailsViewModel, exception = exception)
    }

    private fun viewModelIsInitialized() {
        redditPostDetailsViewModel = RedditPostDetailsViewModel(itemSelectedDispatcher, logger, httpClient)
        redditPostDetailsViewModel.postState.observeForever(postStateObserver)
    }
}
