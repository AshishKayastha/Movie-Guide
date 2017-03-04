package com.ashish.movieguide.utils

import com.ashish.movieguide.BuildConfig
import com.ashish.movieguide.utils.extensions.dpToPx

/**
 * Created by Ashish on Dec 27.
 */
object Constants {

    const val OMDB_API_BASE_URL = "http://www.omdbapi.com/"
    const val TMDB_API_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    const val VALIDATE_TMDB_REQUEST_TOKEN_URL = "https://www.themoviedb.org/authenticate/"

    const val POSTER_W500_URL_PREFIX = TMDB_BASE_IMAGE_URL + "w500"
    const val BACKDROP_W1280_URL_PREFIX = TMDB_BASE_IMAGE_URL + "w1280"
    const val ORIGINAL_IMAGE_URL_PREFIX = TMDB_BASE_IMAGE_URL + "original"

    const val API_KEY = "api_key"
    const val LANGUAGE = "language"
    const val US_ENGLISH = "en-US"
    const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY

    const val MEDIA_TYPE_TV = "tv"
    const val MEDIA_TYPE_MOVIE = "movie"
    const val MEDIA_TYPE_PERSON = "person"
    const val INCLUDE_IMAGE_LANGUAGE = "?include_image_language=en,null"

    const val DATE_PICKER_FORMAT = "%d-%d-%d"
    const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
    const val MONTH_DAY_YEAR_PATTERN = "MMM dd, yyyy"

    const val IMDB_BASE_URL = "http://www.imdb.com/title/"

    const val ADAPTER_TYPE_MOVIE = 0
    const val ADAPTER_TYPE_TV_SHOW = 1
    const val ADAPTER_TYPE_PERSON = 2
    const val ADAPTER_TYPE_CREDIT = 3
    const val ADAPTER_TYPE_SEASON = 4
    const val ADAPTER_TYPE_EPISODE = 5
    const val ADAPTER_TYPE_MULTI_SEARCH = 6

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

    @JvmStatic val THUMBNAIL_SIZE = 180f.dpToPx().toInt()

    @JvmStatic val THUMBNAIL_WIDTH = 200f.dpToPx().toInt()

    @JvmStatic val THUMBNAIL_HEIGHT = 260f.dpToPx().toInt()
}