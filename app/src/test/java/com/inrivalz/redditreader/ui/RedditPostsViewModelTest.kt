package com.inrivalz.redditreader.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.testutil.aRedditPost
import com.inrivalz.redditreader.util.Logger
import com.nhaarman.mockitokotlin2.argWhere
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test

class RedditPostsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val savedStateHandle = mock<SavedStateHandle>()
    private val dispatcher = ItemSelectedDispatcherImpl<RedditPost>()
    private val logger = mock<Logger>()

    private lateinit var redditPostsViewModel: RedditPostsViewModel

    private val eventObserver = mock<Observer<RedditPostsViewModel.UiEvent>>()

    private fun viewModelIsInitialized(disp: ItemSelectedDispatcher<RedditPost> = dispatcher) {
        redditPostsViewModel = RedditPostsViewModel(savedStateHandle, disp, logger)
        redditPostsViewModel.uiEvent.observeForever(eventObserver)
    }

    @Test
    fun `Should emit show details state when an item is selected by dispatcher`() {
        val post = aRedditPost()
        viewModelIsInitialized()

        dispatcher.onItemSelected(post)

        verify(eventObserver).onChanged(argWhere { it is RedditPostsViewModel.UiEvent.ShowDetails })
    }

    @Test
    fun `Should log error state and not emit nothing if the dispatcher emits an error`() {
        val exception = Exception("Error")
        val mockDispatcher = mock<ItemSelectedDispatcher<RedditPost>> {
            on { selectedItemStream } doReturn Observable.error(exception)
        }
        viewModelIsInitialized(mockDispatcher)

        verify(eventObserver, never()).onChanged(eq(RedditPostsViewModel.UiEvent.ShowDetails))
        verify(logger).error(redditPostsViewModel, exception = exception)
    }

}
