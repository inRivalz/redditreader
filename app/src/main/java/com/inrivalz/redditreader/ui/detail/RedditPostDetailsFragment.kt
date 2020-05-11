package com.inrivalz.redditreader.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.util.exhaustive
import com.inrivalz.redditreader.util.lifecycleViewModel
import com.inrivalz.redditreader.util.nonNullObserve
import com.inrivalz.redditreader.util.openFile
import com.inrivalz.redditreader.util.toPrettyDate
import com.inrivalz.redditreader.util.toThousandString
import kotlinx.android.synthetic.main.fragment_reddit_post_details.*
import java.io.File


class RedditPostDetailsFragment : Fragment(R.layout.fragment_reddit_post_details) {

    private val viewModel: RedditPostDetailsViewModel by lifecycleViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePostState()
        observeUiEvents()
        vPostImage.setOnClickListener {
            val file = File(requireContext() .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), FILENAME)
            viewModel.openImage(file, viewModel.postState.value!!.thumbnail!!)
        }
    }

    private fun observePostState() {
        viewModel.postState.nonNullObserve(this) { post ->
            populatePost(post)
        }
    }

    private fun observeUiEvents() {
        viewModel.uiEvent.nonNullObserve(this) {
            when (it) {
                is RedditPostDetailsViewModel.UiEvent.OpenBrowser -> openBrowser(it.url)
                is RedditPostDetailsViewModel.UiEvent.OpenFile -> requireActivity().openFile(it.file)
            }.exhaustive
        }
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

            vPostImage.setOnClickListener {
                val file = File(requireContext() .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), FILENAME)
                viewModel.openImage(file, thumbnail)
            }
        }
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
        startActivity(intent)
    }

    companion object {
        private const val FILENAME = "image.jpg"
        fun newInstance(): Fragment = RedditPostDetailsFragment()
    }
}
