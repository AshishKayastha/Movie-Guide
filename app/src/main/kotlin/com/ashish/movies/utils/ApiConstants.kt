package com.ashish.movies.utils

import com.ashish.movies.BuildConfig

/**
 * Created by Ashish on Jan 02.
 */
object ApiConstants {
    const val API_KEY = "api_key"
    const val LANGUAGE = "language"
    const val US_ENGLISH = "en-US"
    const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY

    const val MEDIA_TYPE_TV = "movie"
    const val MEDIA_TYPE_MOVIE = "movie"
    const val MEDIA_TYPE_PERSON = "person"
    const val CREDITS_AND_SIMILAR = "credits,similar"
}