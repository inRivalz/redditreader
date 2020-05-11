package com.inrivalz.redditreader.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.repository.PagedRepositoryResponse
import com.inrivalz.redditreader.repository.RedditPostsRepository
import com.inrivalz.redditreader.testutil.RxTrampolineSchedulerRule
import com.inrivalz.redditreader.testutil.aRedditPost
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import com.inrivalz.redditreader.util.Logger
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argWhere
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test

class RedditPostListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val rxRule = RxTrampolineSchedulerRule()

    private val pagedDataMock = mock<Observable<PagedList<RedditPost>>>()
    private val networkStateMock = mock<Observable<NetworkState>>()
    private val onRetryMock = mock<() -> Unit>()

    private val itemSelectorDispatcher = mock<ItemSelectedDispatcher<RedditPost>>()
    private val redditPostsRepository = mock<RedditPostsRepository>()
    private val logger = mock<Logger>()

    private lateinit var redditPostListViewModel: RedditPostListViewModel

    private val listStateObserver = mock<Observer<List<RedditPost>>>()
    private val listNetworkState = mock<Observer<NetworkState>>()
    private val refreshNetworkState = mock<Observer<NetworkState>>()

    @Test
    fun `Should call repository on creation`() {
        repositoryReturnsAPagedResponse()

        viewModelIsInitialized()

        verify(redditPostsRepository).getTopPosts(20)
    }

    @Test
    fun `Should emit paged data on repository response`() {
        val pagedList = mock<PagedList<RedditPost>>()
        repositoryReturnsAPagedResponse(pagedData = Observable.just(pagedList))

        viewModelIsInitialized()

        verify(listStateObserver).onChanged(argWhere { it == pagedList })
    }

    @Test
    fun `Should not emit paged data and log error on repository response with error data`() {
        val exception = Exception("Error")
        repositoryReturnsAPagedResponse(pagedData = Observable.error(exception))

        viewModelIsInitialized()

        verify(listStateObserver, never()).onChanged(any())
        verify(logger).error(redditPostListViewModel, exception = exception)
    }

    @Test
    fun `Should emit paged network state on repository response`() {
        repositoryReturnsAPagedResponse(networkState = Observable.just(NetworkState.Success))

        viewModelIsInitialized()

        verify(listNetworkState).onChanged(argWhere { it == NetworkState.Success })
    }

    @Test
    fun `Should emit failure paged list state and log error on repository response with error data`() {
        val exception = Exception("Error")
        repositoryReturnsAPagedResponse(networkState = Observable.error(exception))

        viewModelIsInitialized()

        verify(listNetworkState).onChanged(argWhere { it == NetworkState.Failure })
        verify(logger).error(redditPostListViewModel, exception = exception)
    }

    @Test
    fun `Should emit loading state when triggering refresh`() {
        repositoryReturnsAPagedResponse()
        viewModelIsInitialized()
        whenever(redditPostsRepository.refreshPosts()).doReturn(Completable.never())

        redditPostListViewModel.refresh()
        
        verify(refreshNetworkState).onChanged(argWhere { it == NetworkState.Loading })
    }

    @Test
    fun `Should emit success state when triggering refresh and network is OK`() {
        repositoryReturnsAPagedResponse()
        viewModelIsInitialized()
        whenever(redditPostsRepository.refreshPosts()).doReturn(Completable.complete())

        redditPostListViewModel.refresh()

        refreshNetworkState.inOrder {
            verify().onChanged(argWhere { it == NetworkState.Loading })
            verify().onChanged(argWhere { it == NetworkState.Success })
        }
    }


    @Test
    fun `Should emit error state when triggering refresh and there is network error`() {
        repositoryReturnsAPagedResponse()
        viewModelIsInitialized()
        whenever(redditPostsRepository.refreshPosts()).doReturn(Completable.error(Exception("error")))

        redditPostListViewModel.refresh()

        refreshNetworkState.inOrder {
            verify().onChanged(argWhere { it == NetworkState.Loading })
            verify().onChanged(argWhere { it == NetworkState.Failure })
        }
    }

    @Test
    fun `Should delegate call to dispatcher when an item is selected`() {
        val redditPost = aRedditPost()
        repositoryReturnsAPagedResponse()
        viewModelIsInitialized()

        redditPostListViewModel.onItemSelected(redditPost)

        verify(itemSelectorDispatcher).onItemSelected(redditPost)
    }

    private fun viewModelIsInitialized() {
        redditPostListViewModel =
            RedditPostListViewModel(itemSelectorDispatcher, redditPostsRepository, logger)
        redditPostListViewModel.listState.observeForever(listStateObserver)
        redditPostListViewModel.listNetworkState.observeForever(listNetworkState)
        redditPostListViewModel.refreshState.observeForever(refreshNetworkState)
    }

    private fun repositoryReturnsAPagedResponse(
        pagedData: Observable<PagedList<RedditPost>> = Observable.never(),
        networkState: Observable<NetworkState> = Observable.never(),
        onRetry: () -> Unit = mock()
    ): PagedRepositoryResponse {
        val response = PagedRepositoryResponse(pagedData, networkState, onRetry)
        whenever(redditPostsRepository.getTopPosts(any())).doReturn(response)
        return response
    }
}
