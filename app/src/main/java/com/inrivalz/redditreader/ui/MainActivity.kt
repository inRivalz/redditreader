package com.inrivalz.redditreader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.ui.list.RedditPostListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.vRedditFragmentContainer, RedditPostListFragment.newInstance())
            .commit()
    }
}
