package com.ashish.movies.utils

import com.ashish.movies.utils.extensions.dpToPx

/**
 * Created by Ashish on Dec 27.
 */
object Constants {
    const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
    const val IMDB_BASE_URL = "http://www.imdb.com/title/"
    const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

    const val ADAPTER_TYPE_MOVIE = 0
    const val ADAPTER_TYPE_TV_SHOW = 1
    const val ADAPTER_TYPE_PERSON = 2
    const val ADAPTER_TYPE_CREDIT = 3
    const val ADAPTER_TYPE_SEASON = 4
    const val ADAPTER_TYPE_EPISODE = 5
    const val ADAPTER_TYPE_MULTI_SEARCH = 6

    @JvmStatic val THUMBNAIL_SIZE = 180f.dpToPx().toInt()
}