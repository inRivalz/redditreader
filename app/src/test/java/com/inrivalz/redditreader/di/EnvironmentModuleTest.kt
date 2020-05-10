package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.network.ServerEnvironment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito

class EnvironmentModuleTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(environmentModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> Mockito.mock(clazz.java) }

    @Test
    fun `Should return the same server environment instance`() {
        val serverEnvironment = inject<ServerEnvironment>().value
        val serverEnvironment2 = inject<ServerEnvironment>().value

        assertThat(serverEnvironment).isEqualTo(serverEnvironment2)
    }
}
