package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import com.inrivalz.redditreader.ui.ItemSelectedDispatcherImpl
import com.inrivalz.redditreader.ui.RedditPostsActivity
import com.inrivalz.redditreader.ui.RedditPostsViewModel
import com.inrivalz.redditreader.ui.detail.RedditPostDetailsFragment
import com.inrivalz.redditreader.ui.detail.RedditPostDetailsViewModel
import com.inrivalz.redditreader.ui.list.RedditPostListFragment
import com.inrivalz.redditreader.ui.list.RedditPostListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    scope<RedditPostsActivity> {
        scoped { ItemSelectedDispatcherImpl<RedditPost>() as ItemSelectedDispatcher<RedditPost> }
        viewModel { RedditPostsViewModel(get(), get()) }

        scope<RedditPostListFragment> {
            viewModel { RedditPostListViewModel(get()) }
        }

        scope<RedditPostDetailsFragment> {
            viewModel { RedditPostDetailsViewModel(get()) }
        }
    }
}
