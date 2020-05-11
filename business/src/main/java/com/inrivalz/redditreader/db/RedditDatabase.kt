package com.inrivalz.redditreader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.business.entities.RedditPostState

@Database(entities = [RedditPost::class, RedditPostState::class], version = 1, exportSchema = false)
internal abstract class RedditDatabase : RoomDatabase() {

    abstract fun redditPostDao(): RedditPostsDao

    companion object {
        const val DATABASE_NAME = "reddit-database"
    }
}
