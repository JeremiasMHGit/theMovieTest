package com.example.themovietest.view.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.themovietest.model.remote.Repository
import com.example.themovietest.viewmodel.MoviesViewModel

class MoviesFactory (
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MoviesViewModel::class.java)){
            MoviesViewModel(
                repository
            ) as T
        }else throw IllegalArgumentException("No se encontró la clase")
    }
}