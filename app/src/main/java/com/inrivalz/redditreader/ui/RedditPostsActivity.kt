package com.inrivalz.redditreader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.inrivalz.redditreader.R
import com.inrivalz.redditreader.ui.detail.RedditPostDetailsFragment
import com.inrivalz.redditreader.ui.list.RedditPostListFragment
import com.inrivalz.redditreader.util.exhaustive
import com.inrivalz.redditreader.util.nonNullObserve
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel

class RedditPostsActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: RedditPostsViewModel by lifecycleScope.viewModel(this)

    private val fragmentPagerAdapter: MasterDetailFragmentAdapter by lazy {
        MasterDetailFragmentAdapter(
            this,
            RedditPostListFragment.Companion::newInstance,
            RedditPostDetailsFragment.Companion::newInstance
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(vToolbar)
        initViewPager()
        observeUiState()
        observeUiEvents()
    }

    private fun initViewPager() {
        vMasterDetailPager.adapter = fragmentPagerAdapter
        vMasterDetailPager.isUserInputEnabled = false
        vMasterDetailPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                invalidateOptionsMenu()
            }
        })
    }

    private fun observeUiEvents() {
        viewModel.uiEvent.nonNullObserve(this) { event ->
            when (event) {
                RedditPostsViewModel.UiEvent.ExitApplication -> finish()
            }.exhaustive
        }
    }

    private fun observeUiState() {
        viewModel.uiState.nonNullObserve(this) { state ->
            when (state) {
                RedditPostsViewModel.UiState.ShowMaster -> {
                    vMasterDetailPager.currentItem = MasterDetailFragmentAdapter.MASTER_POSITION
                }
                RedditPostsViewModel.UiState.ShowDetails -> {
                    vMasterDetailPager.currentItem = MasterDetailFragmentAdapter.DETAIL_POSITION
                }
            }.exhaustive
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
