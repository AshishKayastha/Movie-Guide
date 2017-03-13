package com.ashish.movieguide.utils

import com.ashish.movieguide.utils.extensions.dpToPx

/**
 * Created by Ashish on Dec 27.
 */
object Constants {

    const val TMDB_URL = "https://www.themoviedb.org/"
    const val IMDB_BASE_URL = "http://www.imdb.com/title/"
    const val OMDB_API_BASE_URL = "http://www.omdbapi.com/"
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

    const val DATE_PICKER_FORMAT = "%d-%d-%d"
    const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
    const val MONTH_DAY_YEAR_PATTERN = "MMM dd, yyyy"

    const val ADAPTER_TYPE_MOVIE = 0
    const val ADAPTER_TYPE_TV_SHOW = 1
    const val ADAPTER_TYPE_PERSON = 2
    const val ADAPTER_TYPE_CREDIT = 3
    const val ADAPTER_TYPE_SEASON = 4
    const val ADAPTER_TYPE_EPISODE = 5
    const val ADAPTER_TYPE_MULTI_SEARCH = 6
    const val ADAPTER_TYPE_REVIEW = 7

    // Personal list types
    const val FAVORITES = 0
    const val WATCHLIST = 1

    // Types of personal list for movies and tv shows
    @JvmStatic
    val PERSONAL_CONTENT_TYPES = arrayOf("favorite", "watchlist")

    @JvmStatic
    val SORT_BY_MOVIE = arrayOf(
            "popularity.desc",
            "release_date.desc",
            "original_title.desc",
            "vote_average.desc"
    )

    @JvmStatic
    val SORT_BY_TV_SHOW = arrayOf(
            "popularity.desc",
            "first_air_date.desc",
            "vote_average.desc"
    )

    @JvmStatic val DETAIL_IMAGE_THUMBNAIL_SIZE = 180f.dpToPx().toInt()

    @JvmStatic val LIST_THUMBNAIL_WIDTH = 200f.dpToPx().toInt()

    @JvmStatic val LIST_THUMBNAIL_HEIGHT = 260f.dpToPx().toInt()
}