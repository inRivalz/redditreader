package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.network.ServerEnvironment
import com.inrivalz.redditreader.network.ServerEnvironmentImpl
import com.inrivalz.redditreader.util.Logger
import com.inrivalz.redditreader.util.LoggerImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

val environmentModule = module {
    single { HttpClient(Android) }
    single { ServerEnvironmentImpl() as ServerEnvironment }
    single { LoggerImpl() as Logger }
}
