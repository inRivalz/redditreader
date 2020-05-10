package com.inrivalz.redditreader.business.backend

import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.api.RedditApi
import com.inrivalz.redditreader.network.data.JsonRedditListing
import com.inrivalz.redditreader.network.data.JsonRedditListingChild
import com.inrivalz.redditreader.network.data.JsonRedditListingData
import com.inrivalz.redditreader.network.data.JsonRedditPost
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class RedditBackendImplTest {

    private val redditApi: RedditApi = mock()

    private lateinit var redditBackendImpl: RedditBackendImpl

    @Before
    fun setUp() {
        redditBackendImpl = RedditBackendImpl(redditApi)
    }

    @Test
    fun `Should map response from api to business entities on get top`() {
        val response = aJsonRedditListing()
        whenever(redditApi.getTop(30)).doReturn(Single.just(response))

        val testObserver = redditBackendImpl.getTop(30).test()

        verify(redditApi).getTop(30)
        testObserver.assertValue(listOf(
            RedditPost(title = "Post Title", author = "Author", created = 0, thumbnail = "thumbnail")
        ))
    }

    @Test
    fun `Should map response from api to business entities on get top after`() {
        val response = aJsonRedditListing()
        whenever(redditApi.getTopAfter("after", 20)).doReturn(Single.just(response))

        val testObserver = redditBackendImpl.getTopAfter("after", 20).test()

        verify(redditApi).getTopAfter("after", 20)
        testObserver.assertValue(listOf(
            RedditPost(title = "Post Title", author = "Author", created = 0, thumbnail = "thumbnail")
        ))
    }

    @Test
    fun `Should map response in order to business entities on get top`() {
        val response = aJsonRedditListing(listOf(
            aJsonRedditListingChild(suffix = "1"), aJsonRedditListingChild(suffix = "2")
        ))
        whenever(redditApi.getTop(10)).doReturn(Single.just(response))

        val testObserver = redditBackendImpl.getTop(10).test()

        verify(redditApi).getTop(10)
        testObserver.assertValue(listOf(
            RedditPost(title = "Post Title1", author = "Author1", created = 0, thumbnail = "thumbnail"),
            RedditPost(title = "Post Title2", author = "Author2", created = 0, thumbnail = "thumbnail")
        ))
    }

    private fun aJsonRedditListingChild(suffix: String = ""): JsonRedditListingChild {
        return JsonRedditListingChild(
            JsonRedditPost(
                title = "Post Title$suffix",
                author = "Author$suffix",
                created = 0L,
                thumbnail = "thumbnail"
            )
        )
    }

    private fun aJsonRedditListing(
        posts: List<JsonRedditListingChild> = listOf(aJsonRedditListingChild())
    ): JsonRedditListing {
        return JsonRedditListing(JsonRedditListingData(posts, "after"))
    }
}
