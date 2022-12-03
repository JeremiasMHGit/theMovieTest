package com.example.themovietest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.themovietest.model.entity.Movie
import com.example.themovietest.model.entity.MoviesResponse
import com.example.themovietest.model.entity.UpcomingMoviesResponse
import com.example.themovietest.model.remote.Repository
import com.example.themovietest.model.remote.network.Failure
import com.example.themovietest.utils.NetworkState
import com.example.themovietest.model.remote.network.Result
import com.example.themovietest.view.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MoviesViewModel (private val repository: Repository) : BaseViewModel(repository)  {
    var mMoviesMLiveData = MutableLiveData<List<Movie>>()
    val mMoviesLiveData: LiveData<List<Movie>> get() = mMoviesMLiveData

    private var upcomingMoviesMLiveData = MutableLiveData<List<Movie>>()
    val upcomingMoviesLiveData: LiveData<List<Movie>> get() = upcomingMoviesMLiveData

    fun retrievePopularMovies() {
        viewModelScope.launch(handler) {
            networkStatus.postValue(NetworkState.LOADING)
            repository.getPopularMovies().let { setResults(it) }
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch(handler) {
            networkStatus.postValue(NetworkState.LOADED)
            setResults(repository.getTopRatedMovies())
        }
    }

    fun getUpcomingMovies() {
        viewModelScope.launch(handler) {
            networkStatus.postValue(NetworkState.LOADED)
            setResultUpComing(repository.getUpcomingMovies())
        }
    }

    private fun setResults(moviesResult: Result<MoviesResponse>) {
        when (moviesResult) {
            is Result.Success -> {
                if (moviesResult.data != null) {
                    mMoviesMLiveData.postValue(moviesResult.data.results)
                }
            }
            else -> networkStatus.postValue(NetworkState.error("Failed to get data"))
        }
    }

    private fun setResultUpComing(moviesResult: Result<UpcomingMoviesResponse>) {
        when (moviesResult) {
            is Result.Success -> {
                if (moviesResult.data != null) {
                    upcomingMoviesMLiveData.postValue(moviesResult.data.results)
                }
            }
            is Result.Error -> {
                networkStatus.postValue(NetworkState.error("Failed to get data"))
            }
            else -> {}
        }
    }
}