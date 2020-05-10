package com.inrivalz.redditreader.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.nhaarman.mockitokotlin2.argWhere
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RedditPostListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var redditPostListViewModel: RedditPostListViewModel

    private val listStateObserver = mock<Observer<List<RedditPost>>>()
    private val networkStateObserver = mock<Observer<NetworkState>>()

    @Before
    fun setUp() {
        redditPostListViewModel = RedditPostListViewModel()
        redditPostListViewModel.listState.observeForever(listStateObserver)
        redditPostListViewModel.networkState.observeForever(networkStateObserver)
    }

    @Test
    fun `Should emit list state with mocked data on creation`() {
        verify(listStateObserver).onChanged(check {
            assertThat(it).hasSize(10)
            assertThat(it.first().title).isEqualTo("Post Title")
        })
    }

    @Test
    fun `Should emit network state on refresh`() {
        redditPostListViewModel.refresh()

        verify(networkStateObserver).onChanged(argWhere { it is NetworkState.Success })
    }
}
