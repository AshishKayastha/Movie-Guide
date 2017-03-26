package com.ashish.movieguide.utils

object TMDbConstants {

    const val TMDB_URL = "https://www.themoviedb.org/"
    const val TMDB_API_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"

    // Image Url Configurations
    const val STILL_URL_PREFIX = TMDB_BASE_IMAGE_URL + "w300"
    const val POSTER_URL_PREFIX = TMDB_BASE_IMAGE_URL + "w342"
    const val PROFILE_URL_PREFIX = TMDB_BASE_IMAGE_URL + "h632"
    const val BACKDROP_URL_PREFIX = TMDB_BASE_IMAGE_URL + "w780"
    const val ORIGINAL_IMAGE_URL_PREFIX = TMDB_BASE_IMAGE_URL + "original"

    const val LARGE_POSTER_URL_PREFIX = TMDB_BASE_IMAGE_URL + "w500"
    const val LARGE_BACKDROP_URL_PREFIX = TMDB_BASE_IMAGE_URL + "w1280"

    const val MEDIA_TYPE_TV = "tv"
    const val MEDIA_TYPE_MOVIE = "movie"
    const val MEDIA_TYPE_PERSON = "person"
    const val INCLUDE_IMAGE_LANGUAGE = "?include_image_language=en,null"

    // Personal list types
    const val FAVORITES = 0
    const val WATCHLIST = 1

    // Types of personal list for movies and tv shows
    val PERSONAL_CONTENT_TYPES = arrayOf("favorite", "watchlist")

    val SORT_BY_MOVIE = arrayOf(
            "popularity.desc",
            "release_date.desc",
            "original_title.desc",
            "vote_average.desc"
    )

    val SORT_BY_TV_SHOW = arrayOf(
            "popularity.desc",
            "first_air_date.desc",
            "vote_average.desc"
    )
}