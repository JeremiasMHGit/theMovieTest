package com.example.themovietest.model.local.sharedpref


interface SharedPreferenceHelper {
    fun getGuestSession(): String?
    fun setGuestSession(guestSessionId: String)
    fun getGuestSessionExpiration(): String?
    fun setGuestSessionExpiration(guestSessionExpiration: String)
    fun addFavToMovie(movieId: String, isFav: Boolean)
    fun isFavMovie(movieId: String): Boolean
}