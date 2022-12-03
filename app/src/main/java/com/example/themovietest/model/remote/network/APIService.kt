package com.example.themovietest.model.remote.network

import com.example.themovietest.model.entity.*
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(): Response<MoviesResponse>

    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(): Response<UpcomingMoviesResponse>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(): Response<MoviesResponse>


    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetails( @Path("movie_id") movie_id: Int): Response<MovieOverview>


    @GET("/3/movie/{movie_id}/credits")
    suspend fun getCredits(@Path("movie_id") movie_id: Int): Response<Credit>


    @GET("/3/movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") movie_id: Int): Response<MoviesResponse>

    @POST("/3/movie/{movie_id}/rating")
    suspend fun rateMovie(
        @Path("movie_id") movie_id: Int,
        @Query("guest_session_id") guest_session_id: String,
        @Body value: Float
    ): Response<RatingResponse>


    @GET("/3/authentication/guest_session/new")
    suspend fun getGuestSessionId(): Response<GuestSession>
}