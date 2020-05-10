package com.inrivalz.redditreader.ui

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface ItemSelectedDispatcher<T> {
    val selectedItemStream: Observable<T>
    fun onItemSelected(item: T)
}

class ItemSelectedDispatcherImpl<T> : ItemSelectedDispatcher<T> {
    private val itemBehaviorSubject = BehaviorSubject.create<T>()
    override val selectedItemStream: Observable<T> = itemBehaviorSubject.hide()

    override fun onItemSelected(item: T) {
        itemBehaviorSubject.onNext(item)
    }
}
