package com.inrivalz.redditreader.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.inrivalz.redditreader.network.ServerEnvironment
import com.inrivalz.redditreader.network.api.RedditApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideGson() }
    factory { provideOkHttpClient() }
    factory { provideRetrofit(get(), get(), get()) }
    single { provideRedditApi(get()) }
}

internal fun provideRetrofit(environment: ServerEnvironment, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(environment.getApiRedditUrl())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

internal fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()
}

internal fun provideGson(): Gson = GsonBuilder().create()

internal fun provideRedditApi(retrofit: Retrofit): RedditApi = retrofit.create(RedditApi::class.java)
