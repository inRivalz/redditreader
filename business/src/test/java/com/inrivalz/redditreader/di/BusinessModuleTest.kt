package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.network.api.RedditApi
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito

class BusinessModuleTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(businessModule, apiModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> Mockito.mock(clazz.java) }

    private val redditApiMock = mock<RedditApi>()
    private val apiModule = module {
        single { redditApiMock }
    }

    @Test
    fun `Should return the same reddit backend instance`() {
        val redditBackend = inject<RedditBackend>().value
        val redditBackend2 = inject<RedditBackend>().value

        assertThat(redditBackend).isEqualTo(redditBackend2)
    }

}