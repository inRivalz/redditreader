package com.inrivalz.redditreader.ui.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.network.NetworkState
import com.inrivalz.redditreader.util.InnerDividerItemDecoration
import com.inrivalz.redditreader.util.InnerDividerItemDecoration.Companion.VERTICAL
import com.inrivalz.redditreader.util.lifecycleViewModel
import kotlinx.android.synthetic.main.fragment_reddit_post_list.*

class RedditPostListFragment : Fragment(R.layout.fragment_reddit_post_list) {

    private val viewModel: RedditPostListViewModel by lifecycleViewModel()
    private val adapter: RedditPostAdapter by lazy { RedditPostAdapter(::onPostSelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initSwipeRefreshLayout()
        initRecyclerView()
        observeViewModelState()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_posts_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_item_dismiss_all -> {
                viewModel.deleteAll()
                true
            }
            R.id.action_item_undo_dismiss -> {
                viewModel.clearAllDismissed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        vPostRecycler.adapter = adapter
        val helper = ItemTouchHelper(SwipeToDeleteCallback(requireContext(), ::onItemDeleted))
        helper.attachToRecyclerView(vPostRecycler)
        requireContext().getDrawable(R.drawable.item_separator)?.let { divider ->
            vPostRecycler.addItemDecoration(InnerDividerItemDecoration(VERTICAL, divider))
        }
    }

    private fun initSwipeRefreshLayout() {
        vSwipeRefresh.setOnRefreshListener { viewModel.refresh() }
        viewModel.refreshState.observe(this, Observer {
            vSwipeRefresh.isRefreshing = it == NetworkState.Loading
        })
        refreshInitial()
    }

    private fun refreshInitial() {
        vSwipeRefresh.isRefreshing = true
        viewModel.refresh()
    }

    private fun observeViewModelState() {
        viewModel.listState.observe(this, Observer { posts ->
            adapter.submitList(posts)
        })
    }

    private fun onPostSelected(redditPost: RedditPost) {
        viewModel.onItemSelected(redditPost)
    }

    private fun onItemDeleted(position: Int) {
        viewModel.onItemDeleted(position)
    }

    companion object {
        fun newInstance(): Fragment = RedditPostListFragment()
    }
}
