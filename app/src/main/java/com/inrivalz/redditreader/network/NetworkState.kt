package com.inrivalz.redditreader.network

sealed class NetworkState {
    object Loading : NetworkState()
    object Success : NetworkState()
    object Failure : NetworkState()
}