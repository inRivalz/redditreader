package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.business.entities.RedditPost
import com.inrivalz.redditreader.ui.ItemSelectedDispatcher
import com.inrivalz.redditreader.ui.MainActivity
import com.inrivalz.redditreader.ui.detail.RedditPostDetailsFragment
import com.inrivalz.redditreader.ui.detail.RedditPostDetailsViewModel
import com.inrivalz.redditreader.ui.list.RedditPostListFragment
import com.inrivalz.redditreader.ui.list.RedditPostListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    scope<MainActivity> {
        scoped { ItemSelectedDispatcher<RedditPost>() }

        scope<RedditPostListFragment> {
            viewModel { RedditPostListViewModel(get()) }
        }

        scope<RedditPostDetailsFragment> {
            viewModel { RedditPostDetailsViewModel(get()) }
        }
    }
}
