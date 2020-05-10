package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.network.ServerEnvironment
import com.inrivalz.redditreader.network.ServerEnvironmentImpl
import com.inrivalz.redditreader.util.Logger
import com.inrivalz.redditreader.util.LoggerImpl
import org.koin.dsl.module

val environmentModule = module {
    single { ServerEnvironmentImpl() as ServerEnvironment }
    single { LoggerImpl() as Logger }
}
