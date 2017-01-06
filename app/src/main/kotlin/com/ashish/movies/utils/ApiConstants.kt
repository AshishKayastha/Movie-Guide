package com.ashish.movies.utils

import com.ashish.movies.BuildConfig

/**
 * Created by Ashish on Jan 02.
 */
object ApiConstants {

    const val BASE_API_URL = "https://api.themoviedb.org/3/"
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    const val POSTER_W500_URL_PREFIX = BASE_IMAGE_URL + "w500"
    const val BACKDROP_W780_URL_PREFIX = BASE_IMAGE_URL + "w780"
    const val PROFILE_ORIGINAL_URL_PREFIX = BASE_IMAGE_URL + "original"

    const val API_KEY = "api_key"
    const val LANGUAGE = "language"
    const val US_ENGLISH = "en-US"
    const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY

    const val MEDIA_TYPE_TV = "tv"
    const val MEDIA_TYPE_MOVIE = "movie"
    const val MEDIA_TYPE_PERSON = "person"
    const val CREDITS_AND_SIMILAR = "credits,similar"
}