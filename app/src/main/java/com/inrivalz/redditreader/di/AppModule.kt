package com.inrivalz.redditreader.di

import com.inrivalz.redditreader.ui.list.RedditPostListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { RedditPostListViewModel() }
}