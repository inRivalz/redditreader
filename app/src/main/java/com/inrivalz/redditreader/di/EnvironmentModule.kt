package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.network.ServerEnvironment
import com.inrivalz.redditreader.network.ServerEnvironmentImpl
import org.koin.dsl.module

val environmentModule = module {
    single { ServerEnvironmentImpl() as ServerEnvironment }
}