package com.inrivalz.redditreader.ui.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.util.InnerDividerItemDecoration
import com.inrivalz.redditreader.util.InnerDividerItemDecoration.Companion.VERTICAL
import kotlinx.android.synthetic.main.fragment_reddit_post_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class RedditPostListFragment : Fragment(R.layout.fragment_reddit_post_list) {

    private val viewModel: RedditPostListViewModel by viewModel()
    private val adapter: RedditPostAdapter by lazy { RedditPostAdapter(::onPostSelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeRefreshLayout()
        initRecyclerView()
        observeViewModelState()
    }

    private fun initRecyclerView() {
        vPostRecycler.adapter = adapter
        requireContext().getDrawable(R.drawable.item_separator)?.let { divider ->
            vPostRecycler.addItemDecoration(InnerDividerItemDecoration(VERTICAL, divider))
        }
    }

    private fun initSwipeRefreshLayout() {
        vSwipeRefresh.setOnRefreshListener { viewModel.refresh() }
        viewModel.networkState.observe(this, Observer {
            vSwipeRefresh.isRefreshing = it == NetworkState.Loading
        })
    }

    private fun observeViewModelState() {
        viewModel.listState.observe(this, Observer { posts ->
            adapter.submitList(posts)
        })
    }

    private fun onPostSelected(redditPost: RedditPost) {
        Toast.makeText(requireContext(), redditPost.title, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): Fragment = RedditPostListFragment()
    }
}