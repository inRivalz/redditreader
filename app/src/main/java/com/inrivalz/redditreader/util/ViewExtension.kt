package com.inrivalz.redditreader.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel

inline fun <reified T : View> ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = true
): T = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot) as T

inline fun <reified T : ViewModel> Fragment.lifecycleViewModel() = lazy<T> {
    this.lifecycleScope.linkTo(requireActivity().lifecycleScope)
    lifecycleScope.viewModel<T>(this).value
}

fun <T> LiveData<T>.nonNullObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}
