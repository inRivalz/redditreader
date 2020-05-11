package com.inrivalz.redditreader.repository

import androidx.paging.DataSource
import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.db.RedditPostsDao
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.testutil.aRedditPost
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.Executor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class RedditPostsRepositoryImplTest {

    private val redditPostBoundaryCallbackFactory = mock<RedditPostBoundaryCallbackFactory>()
    private val redditPostsDao = mock<RedditPostsDao>()
    private val redditBackend = mock<RedditBackend>()
    private val ioExecutor = mock<Executor> {
        on { execute(any()) } doAnswer {
            (it.arguments[0] as Runnable).run()
        }
    }

    private lateinit var redditPostsRepository: RedditPostsRepository

    @Before
    fun setUp() {
        redditPostsRepository = RedditPostsRepositoryImpl(
            redditPostBoundaryCallbackFactory,
            redditPostsDao,
            redditBackend,
            ioExecutor
        )
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `Should call dao and return a paged repository response when get top posts is called`() {
        val netObservable = Observable.never<NetworkState>()
        val boundaryCallback = mock<RedditPostBoundaryCallback>() {
            on { networkState } doReturn netObservable
        }
        val pagedListMock = mock<DataSource.Factory<Int, RedditPost>>()
        whenever(redditPostBoundaryCallbackFactory.create(any(), any(), any())).doAnswer {
            (it.arguments[1] as (List<RedditPost>) -> Unit).invoke(listOf())
            boundaryCallback
        }
        whenever(redditPostsDao.getVisiblePosts()).doReturn(pagedListMock)

        val response = redditPostsRepository.getTopPosts(20)

        assertThat(response.networkState).isEqualTo(netObservable)
        verify(redditPostBoundaryCallbackFactory).create(any(), any(), eq(20))
        verify(redditPostsDao).insertSorted(listOf())
    }

    @Test
    fun `Refresh posts should call get Top backend`() {
        whenever(redditBackend.getTop(20)).doReturn(Single.never())

        redditPostsRepository.refreshPosts(20)

        verify(redditBackend).getTop(20)
    }

    @Test
    fun `Refresh posts clean insert posts on get Top success`() {
        val posts = listOf(aRedditPost())
        whenever(redditBackend.getTop(20)).doReturn(Single.just(posts))

        val testObserver = redditPostsRepository.refreshPosts(20).test()

        verify(redditBackend).getTop(20)
        verify(redditPostsDao).cleanAnInsert(posts)
        testObserver.assertComplete()
    }
}
