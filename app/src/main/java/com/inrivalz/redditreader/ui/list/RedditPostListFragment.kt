package com.inrivalz.redditreader.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.inrivalz.redditreader.R
import kotlinx.android.synthetic.main.fragment_reddit_post_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class RedditPostListFragment : Fragment(R.layout.fragment_reddit_post_list) {

    private val viewModel: RedditPostListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() {
        vSwipeRefresh.setOnRefreshListener { viewModel.refresh() }

    }
}