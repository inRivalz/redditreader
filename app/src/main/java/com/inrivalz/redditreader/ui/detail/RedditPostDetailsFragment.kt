package com.inrivalz.redditreader.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.lifecycleViewModel
import com.inrivalz.redditreader.util.toPrettyDate
import com.inrivalz.redditreader.util.toThousandString
import kotlinx.android.synthetic.main.fragment_reddit_post_details.*

class RedditPostDetailsFragment : Fragment(R.layout.fragment_reddit_post_details) {

    private val viewModel: RedditPostDetailsViewModel by lifecycleViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePostState()
    }

    private fun observePostState() {
        viewModel.postState.observe(this, Observer { post ->
            post?.let { populatePost(it) }
        })
    }

    private fun populatePost(post: RedditPost) {
        with(post) {
            vSubReddit.text = getString(R.string.reddit_details_subreddit_template, subredit)
            vPostAuthor.text =
                getString(R.string.reddit_details_user_template, author, created.toPrettyDate())
            vPostTitle.text = title
            vPostComments.text = resources.getQuantityString(
                R.plurals.reddit_post_comments,
                comments,
                comments.toThousandString()
            )
            loadThumbnail(thumbnail)
        }
    }

    private fun loadThumbnail(thumbnail: String?) {
        vPostImage.isVisible = thumbnail != null
        thumbnail?.let {
            Glide.with(this).load(it)
                .fitCenter()
                .placeholder(R.drawable.ic_image_placeholder)
                .into(vPostImage)
        }
    }

    companion object {
        fun newInstance(): Fragment = RedditPostDetailsFragment()
    }
}
