package com.inrivalz.redditreader.ui.list

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.getColorFromAttribute
import com.inrivalz.redditreader.util.inflate
import com.inrivalz.redditreader.util.toPrettyDate
import com.inrivalz.redditreader.util.toThousandString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_reddit_post.*

class RedditPostViewHolder private constructor(
    override val containerView: View,
    private val onItemClicked: (RedditPost) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(post: RedditPost) {
        vPostContainer.setOnClickListener { onItemClicked(post) }
        vTitle.text = post.title
        vSubtitle.text = containerView.resources.getString(
            R.string.reddit_post_subtitle,
            post.author,
            post.created.toPrettyDate()
        )
        vPostComments.text = containerView.resources.getQuantityString(
            R.plurals.reddit_post_comments,
            post.comments,
            post.comments.toThousandString()
        )
        post.thumbnail?.let { loadThumbnail(it) }
        setReadStyle(post.read)
    }

    private fun setReadStyle(read: Boolean) {
        val titleColorAttr = if (read) R.attr.colorTitleRead else R.attr.colorTitleUnread
        vTitle.setTextColor(containerView.context.getColorFromAttribute(titleColorAttr))
        vTitle.typeface = Typeface.create(Typeface.DEFAULT, if (read) Typeface.ITALIC else Typeface.NORMAL)
    }

    private fun loadThumbnail(url: String) {
        val cornerPx =
            containerView.resources.getDimensionPixelSize(R.dimen.reddit_thumbnail_corner)
        Glide.with(containerView).load(url)
            .fitCenter()
            .transform(RoundedCorners(cornerPx))
            .placeholder(R.drawable.ic_image_placeholder)
            .into(vThumbnail)
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: (RedditPost) -> Unit): RedditPostViewHolder {
            return RedditPostViewHolder(
                parent.inflate(R.layout.item_reddit_post, false), onItemClicked
            )
        }
    }
}
