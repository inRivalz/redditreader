package com.inrivalz.redditreader.util

import android.util.Log

class LoggerImpl : Logger {

    override fun error(from: Any, message: String?, exception: Throwable?) {
        Log.e(TAG, "$from.javaClass.simpleName}: ${message ?: exception?.message}", exception)
    }

    companion object {
        private const val TAG = "RedditReader"
    }
}
