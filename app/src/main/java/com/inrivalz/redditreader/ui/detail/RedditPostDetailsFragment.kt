package com.inrivalz.redditreader.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.lifecycleViewModel
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
            vSubReddit.text = "TODO"
            vPostTitle.text = title
            vPostAuthor.text = author
            // TODO: Thumbnail
            vPostComments.text = resources.getQuantityString(R.plurals.reddit_post_comments, comments.toInt(), comments.toInt())
        }
    }

    companion object {
        fun newInstance(): Fragment = RedditPostDetailsFragment()
    }
}
