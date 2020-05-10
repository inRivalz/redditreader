package com.inrivalz.redditreader.util

interface Logger {
    fun error(from: Any, message: String? = null, exception: Throwable? = null)
}
