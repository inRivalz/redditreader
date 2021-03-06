package com.inrivalz.redditreader.di

import android.content.Context
import androidx.room.Room
import com.inrivalz.redditreader.db.RedditDatabase
import com.inrivalz.redditreader.db.RedditPostsDao
import com.inrivalz.redditreader.repository.RedditPostBoundaryCallbackFactory
import com.inrivalz.redditreader.repository.RedditPostsRepository
import com.inrivalz.redditreader.repository.RedditPostsRepositoryImpl
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import org.koin.dsl.module

val databaseModule = module {
    factory { Executors.newSingleThreadExecutor() as Executor }
    single { provideRedditDatabase(get()) }
    single { provideRedditPostsDao(get()) }
    single { RedditPostBoundaryCallbackFactory(get()) }
    single { RedditPostsRepositoryImpl(get(), get(), get(), get()) as RedditPostsRepository }
}

private fun provideRedditDatabase(context: Context): RedditDatabase {
    return Room.databaseBuilder(context, RedditDatabase::class.java, RedditDatabase.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideRedditPostsDao(redditDatabase: RedditDatabase): RedditPostsDao {
    return redditDatabase.redditPostDao()
}
