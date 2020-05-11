package com.inrivalz.redditreader.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.inrivalz.redditreader.business.entities.RedditPost

@Dao
internal abstract class RedditPostsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(redditPosts: List<RedditPost>)

    @Query("SELECT * FROM posts WHERE read = 0 ORDER BY position")
    abstract fun getUnreadPosts(): DataSource.Factory<Int, RedditPost>

    @Query("SELECT MAX(position) FROM posts")
    abstract fun getLastIndex(): Int

    @Query("DELETE FROM posts WHERE read = 0")
    abstract fun deleteUnreadPosts()

    @Query("UPDATE posts SET read = 1 WHERE name = :postName")
    abstract fun markPostAsRead(postName: String)

    @Transaction
    open fun cleanAnInsert(redditPosts: List<RedditPost>) {
        deleteUnreadPosts()
        insertSorted(redditPosts)
    }

    @Transaction
    open fun insertSorted(redditPosts: List<RedditPost>) {
        val startPosition = getLastIndex() + 1
        val sortedList = redditPosts.mapIndexed { index, post ->
            post.copy(position = startPosition + index)
        }
        insert(sortedList)
    }
}
