package com.inrivalz.redditreader.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

inline fun <reified T : View> ViewGroup.inflate(
        @LayoutRes layoutRes: Int,
        attachToRoot: Boolean = true
): T = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot) as T
