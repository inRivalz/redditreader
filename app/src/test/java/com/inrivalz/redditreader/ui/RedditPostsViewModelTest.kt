package com.inrivalz.redditreader.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.inrivalz.redditreader.aRedditPost
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.Logger
import com.nhaarman.mockitokotlin2.argWhere
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test

class RedditPostsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = ItemSelectedDispatcherImpl<RedditPost>()
    private val logger = mock<Logger>()

    private lateinit var redditPostsViewModel: RedditPostsViewModel

    private val stateObserver = mock<Observer<RedditPostsViewModel.UiState>>()
    private val eventObserver = mock<Observer<RedditPostsViewModel.UiEvent>>()

    private fun viewModelIsInitialized(disp: ItemSelectedDispatcher<RedditPost> = dispatcher) {
        redditPostsViewModel = RedditPostsViewModel(disp, logger)
        redditPostsViewModel.uiState.observeForever(stateObserver)
        redditPostsViewModel.uiEvent.observeForever(eventObserver)
    }

    @Test
    fun `Should emit show details state when an item is selected by dispatcher`() {
        val post = aRedditPost()
        viewModelIsInitialized()

        dispatcher.onItemSelected(post)

        verify(stateObserver).onChanged(argWhere { it is RedditPostsViewModel.UiState.ShowDetails })
    }

    @Test
    fun `Should log error state and not emit nothing if the dispatcher emits an error`() {
        val exception = Exception("Error")
        val mockDispatcher = mock<ItemSelectedDispatcher<RedditPost>> {
            on { selectedItemStream } doReturn Observable.error(exception)
        }
        viewModelIsInitialized(mockDispatcher)

        verify(stateObserver, never()).onChanged(eq(RedditPostsViewModel.UiState.ShowDetails))
        verify(logger).error(redditPostsViewModel, exception = exception)
    }

    @Test
    fun `Should emit show master state if the user presses back and the view is in details`() {
        val post = aRedditPost()
        viewModelIsInitialized()
        dispatcher.onItemSelected(post)

        redditPostsViewModel.onBackPressed()

        stateObserver.inOrder {
            verify().onChanged(argWhere { it is RedditPostsViewModel.UiState.ShowDetails })
            verify().onChanged(argWhere { it is RedditPostsViewModel.UiState.ShowMaster })
        }
    }

    @Test
    fun `Should emit exit application state if the user presses back and it is on master`() {
        viewModelIsInitialized()

        redditPostsViewModel.onBackPressed()

        verify(eventObserver).onChanged(argWhere { it is RedditPostsViewModel.UiEvent.ExitApplication })
    }
}
