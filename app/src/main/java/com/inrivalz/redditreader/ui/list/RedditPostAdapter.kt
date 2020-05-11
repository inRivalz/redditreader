package com.inrivalz.redditreader.ui.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inrivalz.redditreader.business.entities.RedditPost

class RedditPostAdapter(
    private val onItemClicked: (RedditPost) -> Unit
) : PagedListAdapter<RedditPost, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RedditPostViewHolder.create(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RedditPostViewHolder) {
            getItem(position)?.let { holder.bind(it) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RedditPost>() {
            override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost) = oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost) = oldItem == newItem
        }
    }
}
