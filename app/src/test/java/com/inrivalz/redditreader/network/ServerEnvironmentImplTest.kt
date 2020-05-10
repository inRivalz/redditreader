package com.inrivalz.redditreader.network

import com.inrivalz.redditreader.BuildConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ServerEnvironmentImplTest {

    private val serverEnvironmentImpl = ServerEnvironmentImpl()

    @Test
    fun `Should return Api reddit build config on get reddit url`() {
        assertThat(serverEnvironmentImpl.getApiRedditUrl()).isEqualTo(BuildConfig.API_REDDIT)
    }
}