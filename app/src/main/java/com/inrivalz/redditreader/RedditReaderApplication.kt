package com.inrivalz.redditreader

import android.app.Application
import com.inrivalz.redditreader.di.environmentModule
import com.inrivalz.redditreader.di.businessModule
import com.inrivalz.redditreader.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RedditReaderApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RedditReaderApplication)
            modules(listOf(
                networkModule,
                businessModule, environmentModule))
        }
    }
}