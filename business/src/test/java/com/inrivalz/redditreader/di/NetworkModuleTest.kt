package com.inrivalz.redditreader.di

import com.google.gson.Gson
import com.inrivalz.redditreader.network.ServerEnvironment
import com.inrivalz.redditreader.network.api.RedditApi
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModuleTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(networkModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> Mockito.mock(clazz.java) }

    @Test
    fun `Should return the same gson instance`() {
        val gson = inject<Gson>().value
        val gson2 = inject<Gson>().value

        assertThat(gson).isEqualTo(gson2)
    }

    @Test
    fun `Should return different OkHttpClient instances`() {
        val okHttpClient = inject<OkHttpClient>().value
        val okHttpClient2 = inject<OkHttpClient>().value

        assertThat(okHttpClient).isNotEqualTo(okHttpClient2)
    }

    @Test
    fun `Should add logging interceptor to OkHttpClient instance`() {
        val okHttpClient = inject<OkHttpClient>().value

        assertThat(okHttpClient.interceptors()).hasSize(1)
        assertThat(okHttpClient.interceptors().first()).isInstanceOf(HttpLoggingInterceptor::class.java)
        assertThat((okHttpClient.interceptors().first() as HttpLoggingInterceptor).level)
            .isEqualTo(HttpLoggingInterceptor.Level.BODY)
    }

    @Test
    fun `Should return different Retrofit instances`() {
        setUpEnvironment()

        val retrofit = inject<Retrofit>().value
        val retrofit2 = inject<Retrofit>().value

        assertThat(retrofit).isNotEqualTo(retrofit2)
    }

    @Test
    fun `Should use environment url for retrofit instance`() {
        val environment = setUpEnvironment()

        val retrofit = inject<Retrofit>().value

        verify(environment).getApiRedditUrl()
        assertThat(retrofit.baseUrl().toString()).isEqualTo("http://test.com/")
    }

    @Test
    fun `Should add converter and adapter factory to url instance`() {
        setUpEnvironment()

        val retrofit = inject<Retrofit>().value

        assertThat(retrofit.converterFactories())
            .extracting<Class<*>> { it.javaClass }.contains(GsonConverterFactory::class.java)
        assertThat(retrofit.callAdapterFactories())
            .extracting<Class<*>> { it.javaClass }.contains(RxJava2CallAdapterFactory::class.java)
    }

    @Test
    fun `Should call retrofit create on provide reddit api`() {
        val apiMock = mock<RedditApi>()
        val retrofit = declareMock<Retrofit> {
            whenever(create(RedditApi::class.java)).doReturn(apiMock)
        }

        val api = inject<RedditApi>().value

        verify(retrofit).create(RedditApi::class.java)
        assertThat(api).isEqualTo(apiMock)
    }

    private fun setUpEnvironment(): ServerEnvironment {
        return declareMock {
            whenever(getApiRedditUrl()).doReturn("http://test.com/")
        }
    }
}
