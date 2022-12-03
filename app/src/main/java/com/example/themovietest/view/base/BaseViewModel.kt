package com.example.themovietest.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovietest.model.remote.Repository
import com.example.themovietest.model.remote.network.Failure
import com.example.themovietest.utils.NetworkState
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel(private val repository: Repository) : ViewModel() {

    internal var networkStatus = MutableLiveData<NetworkState>()
    val mNetworkState: LiveData<NetworkState> get() = networkStatus

    internal var handler = CoroutineExceptionHandler { _, ex ->
        when (ex) {
            is Failure.InvalidAPIKeyException -> {
                networkStatus.postValue(NetworkState.error(ex.message ?: "Invalid API key"))
            }
            is Failure.NetworkException,
            is Failure.ServerException,
            is Failure.NotFoundException,
            is Failure.UnknownException -> {
                networkStatus.postValue(NetworkState.error(ex.message ?: "Unknown Error!!!"))
            }
        }
    }
}