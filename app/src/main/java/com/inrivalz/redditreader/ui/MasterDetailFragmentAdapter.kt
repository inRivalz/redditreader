package com.inrivalz.redditreader.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MasterDetailFragmentAdapter(
    activity: FragmentActivity,
    private val masterFragmentFactory: () -> Fragment,
    private val detailFragmentFactory: () -> Fragment
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            MASTER_POSITION -> masterFragmentFactory()
            DETAIL_POSITION -> detailFragmentFactory()
            else -> throw IllegalStateException("Position $position does not exist!")
        }
    }

    override fun getItemCount(): Int = 2

    companion object {
        const val MASTER_POSITION = 0
        const val DETAIL_POSITION = 1
    }
}
