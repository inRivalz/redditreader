package com.inrivalz.redditreader.util

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

open class BaseViewModel() : ViewModel() {
    protected val disposables by lazy { CompositeDisposable() }

    protected fun Disposable.autoClear(): Disposable {
        disposables += this
        return this
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}