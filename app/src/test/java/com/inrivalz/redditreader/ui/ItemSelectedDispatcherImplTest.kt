package com.inrivalz.redditreader.ui

import org.junit.Test

class ItemSelectedDispatcherImplTest {

    private val itemSelectedDispatcher = ItemSelectedDispatcherImpl<Int>()

    @Test
    fun `Should notify stream when an item is selected after observing`() {
        val observer = itemSelectedDispatcher.selectedItemStream.test()

        itemSelectedDispatcher.onItemSelected(5)

        observer.assertValue(5)
    }

    @Test
    fun `Should notify stream when an item is selected before observing`() {
        itemSelectedDispatcher.onItemSelected(5)

        val observer = itemSelectedDispatcher.selectedItemStream.test()

        observer.assertValue(5)
    }
}
