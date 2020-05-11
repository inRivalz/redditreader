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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(redditPosts: List<RedditPost>)

    @Transaction
    @Query("SELECT a.name, a.title, a.author, a.created, a.thumbnail, a.comments, a.subredit, IFNULL(b.read, 0) as read, a.position " +
            "FROM posts a LEFT JOIN posts_state b ON a.name = b.name " +
            "WHERE b.name IS NULL OR b.dismissed = 0 ORDER BY position")
    abstract fun getVisiblePosts(): DataSource.Factory<Int, RedditPost>

    @Query("SELECT MAX(position) FROM posts")
    abstract fun getLastIndex(): Int

    @Query("DELETE FROM posts")
    abstract fun deleteAllPosts()

    @Query("INSERT OR REPLACE INTO posts_state (name, dismissed, read) VALUES (:postName, 0, 1)")
    abstract fun markPostAsRead(postName: String)

    @Query("INSERT OR REPLACE INTO posts_state (name, dismissed, read) VALUES (:postName, 1, 1)")
    abstract fun markPostAsDismissed(postName: String)

    @Transaction
    open fun cleanAnInsert(redditPosts: List<RedditPost>) {
        deleteAllPosts()
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
