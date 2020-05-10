package com.inrivalz.redditreader.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.inrivalz.redditreader.business.entities.RedditPost

class RedditPostAdapter(
    private val onItemClicked: (RedditPost) -> Unit
) : ListAdapter<RedditPost, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RedditPostViewHolder.create(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RedditPostViewHolder) {
            holder.bind(getItem(position))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RedditPost>() {
            override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost) = oldItem.created == newItem.created

            override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost) = oldItem == newItem
        }
    }
}
