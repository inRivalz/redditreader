package com.inrivalz.redditreader

import android.app.Application
import com.inrivalz.redditreader.di.appModule
import com.inrivalz.redditreader.di.businessModule
import com.inrivalz.redditreader.di.databaseModule
import com.inrivalz.redditreader.di.environmentModule
import com.inrivalz.redditreader.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RedditReaderApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@RedditReaderApplication)
            modules(listOf(networkModule, businessModule, environmentModule, appModule, databaseModule))
        }
    }
}
