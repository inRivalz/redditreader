package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.business.backend.RedditBackend
import com.inrivalz.redditreader.business.backend.RedditBackendImpl
import org.koin.dsl.module

val businessModule = module {
    single { RedditBackendImpl(get()) as RedditBackend }
}
