package com.inrivalz.redditreader.ui

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ItemSelectedDispatcher<T> {

    private val itemBehaviorSubject = BehaviorSubject.create<T>()
    val selectedItemStream: Observable<T> = itemBehaviorSubject.hide()

    fun onItemSelected(item: T) {
        itemBehaviorSubject.onNext(item)
    }
}
