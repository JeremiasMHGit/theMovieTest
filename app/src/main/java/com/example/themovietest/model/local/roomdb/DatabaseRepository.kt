package com.example.themovietest.model.local.roomdb

import com.example.themovietest.model.entity.Movie

interface DatabaseRepository {
    suspend fun addMovie(movie: Movie)
    suspend fun fetchAllMovies(): List<Movie>
    suspend fun deleteMovie(movie: Movie)
}