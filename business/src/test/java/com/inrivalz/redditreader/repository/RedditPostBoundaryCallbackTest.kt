package com.inrivalz.redditreader.repository

import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.testutil.RxTrampolineSchedulerRule
import com.inrivalz.redditreader.testutil.aRedditPost
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RedditPostBoundaryCallbackTest {

    @get:Rule
    val rxRule = RxTrampolineSchedulerRule()

    private val redditBackend = mock<RedditBackend>()
    private val pagingRequestHelper = mock<PagingRequestHelper>()
    private val onPostFetched = mock<(List<RedditPost>) -> Unit>()

    private lateinit var redditPostBoundaryCallback: RedditPostBoundaryCallback

    @Before
    fun setUp() {
        redditPostBoundaryCallback = RedditPostBoundaryCallback(redditBackend, pagingRequestHelper, onPostFetched, 20)
    }

    @Test
    fun `Should run Initial request when on Zero Items Loaded is called`() {
        whenever(redditBackend.getTop(20)).doReturn(Single.never())

        redditPostBoundaryCallback.onZeroItemsLoaded()

        verify(pagingRequestHelper).runIfNotRunning(eq(PagingRequestHelper.RequestType.INITIAL), any())
    }

    @Test
    fun `Should call callback and lambda when initial request is success`() {
        val posts = listOf(aRedditPost())
        val callbackMock = helperRunsRequestWithCallback()
        whenever(redditBackend.getTop(20)).doReturn(Single.just(posts))

        redditPostBoundaryCallback.onZeroItemsLoaded()

        verify(pagingRequestHelper).runIfNotRunning(eq(PagingRequestHelper.RequestType.INITIAL), any())
        verify(onPostFetched).invoke(posts)
        verify(callbackMock).recordSuccess()
    }

    @Test
    fun `Should call callback when initial request is failure`() {
        val exception = Exception("Error")
        val callbackMock = helperRunsRequestWithCallback()
        whenever(redditBackend.getTop(20)).doReturn(Single.error(exception))

        redditPostBoundaryCallback.onZeroItemsLoaded()

        verify(pagingRequestHelper).runIfNotRunning(eq(PagingRequestHelper.RequestType.INITIAL), any())
        verify(onPostFetched, never()).invoke(any())
        verify(callbackMock).recordFailure(exception)
    }

    @Test
    fun `Should run get top after request when on item at end loaded is called`() {
        val item = aRedditPost().copy("after")
        whenever(redditBackend.getTopAfter("after", 20)).doReturn(Single.never())

        redditPostBoundaryCallback.onItemAtEndLoaded(item)

        verify(pagingRequestHelper).runIfNotRunning(eq(PagingRequestHelper.RequestType.AFTER), any())
    }

    @Test
    fun `Should call callback and lambda when item at end loaded request is success`() {
        val item = aRedditPost().copy("after")
        val posts = listOf(aRedditPost())
        val callbackMock = helperRunsRequestWithCallback()
        whenever(redditBackend.getTopAfter("after", 20)).doReturn(Single.just(posts))

        redditPostBoundaryCallback.onItemAtEndLoaded(item)

        verify(pagingRequestHelper).runIfNotRunning(eq(PagingRequestHelper.RequestType.AFTER), any())
        verify(onPostFetched).invoke(posts)
        verify(callbackMock).recordSuccess()
    }

    @Test
    fun `Should call callback when item at end loaded request is failure`() {
        val item = aRedditPost().copy("after")
        val exception = Exception("Error")
        val callbackMock = helperRunsRequestWithCallback()
        whenever(redditBackend.getTopAfter("after", 20)).doReturn(Single.error(exception))

        redditPostBoundaryCallback.onItemAtEndLoaded(item)

        verify(pagingRequestHelper).runIfNotRunning(eq(PagingRequestHelper.RequestType.AFTER), any())
        verify(onPostFetched, never()).invoke(any())
        verify(callbackMock).recordFailure(exception)
    }

    @Test
    fun `Network state should add a listener to helper when observed`() {
        verify(pagingRequestHelper, never()).addListener(any())

        val state = redditPostBoundaryCallback.networkState.test()

        verify(pagingRequestHelper).addListener(any())
        state.assertNotComplete()
    }

    @Test
    fun `Network state should emit Loading if report has running jobs`() {
        val mockReport = mock<PagingRequestHelper.StatusReport> {
            on { hasRunning() } doReturn true
        }
        helperReturnsReportToListeners(mockReport)

        val state = redditPostBoundaryCallback.networkState.test()

        state.assertValue(NetworkState.Loading)
    }

    @Test
    fun `Network state should emit Failure if report has failed jobs`() {
        val mockReport = mock<PagingRequestHelper.StatusReport> {
            on { hasRunning() } doReturn false
            on { hasError() } doReturn true
        }
        helperReturnsReportToListeners(mockReport)

        val state = redditPostBoundaryCallback.networkState.test()

        state.assertValue(NetworkState.Failure)
    }

    @Test
    fun `Network state should emit Success if report does not have running or failed jobs`() {
        val mockReport = mock<PagingRequestHelper.StatusReport> {
            on { hasRunning() } doReturn false
            on { hasError() } doReturn false
        }
        helperReturnsReportToListeners(mockReport)

        val state = redditPostBoundaryCallback.networkState.test()

        state.assertValue(NetworkState.Success)
    }

    private fun helperRunsRequestWithCallback(): PagingRequestHelper.Request.Callback {
        val callbackMock = mock<PagingRequestHelper.Request.Callback>()
        whenever(pagingRequestHelper.runIfNotRunning(any(), any())).thenAnswer {
            (it.arguments[1] as PagingRequestHelper.Request).run(callbackMock)
            true
        }
        return callbackMock
    }

    private fun helperReturnsReportToListeners(report: PagingRequestHelper.StatusReport) {
        whenever(pagingRequestHelper.addListener(any())).doAnswer {
            (it.arguments[0] as PagingRequestHelper.Listener).onStatusChange(report)
            true
        }
    }
}