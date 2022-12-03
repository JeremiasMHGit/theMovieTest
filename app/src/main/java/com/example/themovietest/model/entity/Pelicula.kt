package com.example.themovietest.model.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.*
import kotlinx.android.parcel.Parcelize
@Keep
@Parcelize
@Entity(tableName = "movie_table")
data class Movie(
    var adult: Boolean,
    var backdrop_path: String?,
    var genre_ids: List<Integer>,
    @PrimaryKey
    var id: Int,
    var original_language: String,
    var original_title: String,
    var overview: String?,
    var popularity: Float,
    var poster_path: String?,
    var release_date: String,
    var title: String,
    var video: Boolean,
    var vote_average: Float,
    var vote_count: Int
) : Parcelable

@Keep
@Parcelize
data class MoviesResponse(
    var page: Int,
    var results: List<Movie>,
    var total_results: Int,
    var total_pages: Int
) : Parcelable

@Keep
@Parcelize
data class UpcomingMoviesResponse(
    var dates: MDate,
    var page: Int,
    var results: List<Movie>,
    var total_results: Int,
    var total_pages: Int
) : Parcelable

@Keep
@Parcelize
class MDate(
    var maximum: String,
    var minimum: String
) : Parcelable


@Keep
@Parcelize
data class MovieOverview(
    var adult: Boolean,
    var backdrop_path: String?,
    var belongs_to_collection: String?,
    var budget: Int,
    var genres: List<Genre>,
    var homepage: String?,
    var id: Int,
    var imdb_id: String?,
    var original_language: String,
    var original_title: String,
    var overview: String?,
    var popularity: Float,
    var poster_path: String?,
    var production_companies: List<ProductionCompany>,
    var production_countries: List<ProductionCountry>,
    var release_date: String,
    var revenue: Int,
    var runtime: Int?,
    var spoken_languages: List<SpokenLanguage>,
    var status: String,
    var tagline: String?,
    var title: String,
    var video: Boolean,
    var vote_average: Float,
    var vote_count: Int
) : Parcelable

@Keep
@Parcelize
data class Genre(
    var id: Int,
    var name: String
) : Parcelable

@Keep
@Parcelize
data class ProductionCompany(
    var name: String,
    var id: Int,
    var logo_path: String?,
    var origin_country: String
) : Parcelable

@Keep
@Parcelize
data class ProductionCountry(
    var iso_3166_1: String,
    var name: String
) : Parcelable

@Keep
@Parcelize
data class SpokenLanguage(
    var iso_639_1: String,
    var name: String
) : Parcelable

@Keep
@Parcelize
data class Credit(
    var id: Int,
    var cast: List<Cast>,
    var crew: List<Crew>
) : Parcelable

@Keep
@Parcelize
data class Cast(
    var adult: Boolean,
    var gender: Int?,
    var id: Int,
    var known_for_department: String,
    var name: String,
    var original_name: String,
    var popularity: Float,
    var profile_path: String?,
    var cast_id: Int,
    var character: String,
    var credit_id: String,
    var order: Int

) : Parcelable

@Keep
@Parcelize
data class Crew(
    var adult: Boolean,
    var gender: Int?,
    var id: Int,
    var known_for_department: String,
    var name: String,
    var original_name: String,
    var popularity: Float,
    var profile_path: String?,
    var credit_id: String,
    var department: String,
    var job: String
) : Parcelable

@Keep
data class RatingResponse(
    var status_code: Int,
    var status_message: String
)

@Keep
data class GuestSession(
    var success: Boolean,
    var guest_session_id: String,
    var expires_at: String
)