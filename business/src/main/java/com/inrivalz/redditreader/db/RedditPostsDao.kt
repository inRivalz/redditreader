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

    // Since there are two columns with the same name, Room takes the first one.
    // So put first the one with stores the read state, this is done to avoid naming all the columns in the query.
    @Query("SELECT s.read, p.* FROM posts p LEFT JOIN posts_state s ON p.name = s.name WHERE s.name IS NULL OR s.dismissed = 0 ORDER BY p.position")
    abstract fun getVisiblePosts(): DataSource.Factory<Int, RedditPost>

    @Query("SELECT MAX(position) FROM posts")
    abstract fun getLastIndex(): Int

    @Query("DELETE FROM posts")
    abstract fun deleteAllPosts()

    @Query("INSERT OR REPLACE INTO posts_state (name, dismissed, read) VALUES (:postName, 0, 1)")
    abstract fun markPostAsRead(postName: String)

    @Query("INSERT OR REPLACE INTO posts_state (name, dismissed, read) VALUES (:postName, 1, 1)")
    abstract fun markPostAsDismissed(postName: String)

    @Query("INSERT OR REPLACE INTO posts_state (name, dismissed, read) SELECT a.name, 1, 1 FROM posts a")
    abstract fun markAllAsDismissed()

    @Query("UPDATE posts_state SET dismissed = 0")
    abstract fun clearAllDismissed()

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
