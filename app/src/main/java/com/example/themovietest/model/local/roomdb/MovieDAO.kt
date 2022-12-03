package com.example.themovietest.model.local.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.themovietest.model.entity.Movie

@Dao
interface MovieDAO {

    @Insert
    suspend fun addMovie(movie: Movie)

    @Query("select * from movie_table")
    suspend fun fetchAllMovies(): List<Movie>

    @Delete
    suspend fun deleteMovie(movie: Movie)

}