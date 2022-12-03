package com.example.themovietest.model.local.roomdb

import com.example.themovietest.model.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepositoryImp(private val db: MovieDatabase) : DatabaseRepository {
    override suspend fun addMovie(movie: Movie) {
        withContext(Dispatchers.IO) {
            db.movieDAO().addMovie(movie)
        }
    }

    override suspend fun fetchAllMovies() = withContext(Dispatchers.IO) {
        db.movieDAO().fetchAllMovies()
    }

    override suspend fun deleteMovie(movie: Movie) {
        withContext(Dispatchers.IO) {
            db.movieDAO().deleteMovie(movie)
        }
    }
}