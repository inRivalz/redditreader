package com.inrivalz.redditreader.network

import com.inrivalz.redditreader.BuildConfig

class ServerEnvironmentImpl : ServerEnvironment {
    override fun getApiRedditUrl(): String = BuildConfig.API_REDDIT
}