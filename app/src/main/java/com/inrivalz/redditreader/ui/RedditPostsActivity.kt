package com.inrivalz.redditreader.ui

import android.content.res.Configuration
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
import org.koin.androidx.viewmodel.scope.stateViewModel

class RedditPostsActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: RedditPostsViewModel by lifecycleScope.stateViewModel(this)
    private val twoPanes by lazy { resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }

    private val fragmentPagerAdapter: MasterDetailFragmentAdapter by lazy {
        MasterDetailFragmentAdapter(
            this,
            RedditPostListFragment.Companion::newInstance,
            RedditPostDetailsFragment.Companion::newInstance,
            twoPanes
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(vToolbar)
        initViewPager()
        initSidePanel()
        observeUiEvents()
    }

    private fun initSidePanel() {
        if (twoPanes) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.vFragmentDetailsContainer, RedditPostDetailsFragment.newInstance())
                .commit()
        }
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
                RedditPostsViewModel.UiEvent.ShowDetails -> {
                    vMasterDetailPager.currentItem = MasterDetailFragmentAdapter.DETAIL_POSITION
                }
            }.exhaustive
        }
    }

    override fun onBackPressed() {
        if (vMasterDetailPager.currentItem == MasterDetailFragmentAdapter.MASTER_POSITION) {
            super.onBackPressed()
        } else {
            vMasterDetailPager.currentItem = MasterDetailFragmentAdapter.MASTER_POSITION
        }
    }
}
