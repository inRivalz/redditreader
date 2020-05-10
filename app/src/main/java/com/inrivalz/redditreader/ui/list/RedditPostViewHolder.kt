package com.inrivalz.redditreader.ui.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_reddit_post.*

class RedditPostViewHolder private constructor(
    override val containerView: View,
    private val onItemClicked: (RedditPost) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(post: RedditPost) {
        vPostContainer.setOnClickListener { onItemClicked(post) }
        // TODO: Thumbnail
        vTitle.text = post.title
        vSubtitle.text = containerView.resources.getString(R.string.reddit_post_subtitle, post.author, post.created.toString())
        vComments.text = containerView.resources.getQuantityString(R.plurals.reddit_post_comments, post.comments.toInt(), post.comments)
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: (RedditPost) -> Unit): RedditPostViewHolder {
            return RedditPostViewHolder(
                parent.inflate(R.layout.item_reddit_post, false), onItemClicked
            )
        }
    }
}
