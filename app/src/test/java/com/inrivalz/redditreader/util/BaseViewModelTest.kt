package com.inrivalz.redditreader.util

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import junit.framework.Assert.assertTrue
import org.junit.Test

class BaseViewModelTest {

    private val viewModel = BaseVM()

    @Test
    fun `Should dispose on cleared`() {
        val disposable = Observable.never<Int>().subscribe()
        viewModel.autoClear(disposable)

        viewModel.onCleared()

        assertTrue(disposable.isDisposed)
    }

    class BaseVM : BaseViewModel() {
        fun autoClear(disposable: Disposable) {
            disposable.autoClear()
        }

        public override fun onCleared() {
            super.onCleared()
        }
    }
}
