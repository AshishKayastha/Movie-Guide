package com.ashish.movieguide.utils

import com.ashish.movieguide.utils.extensions.dpToPx

/**
 * Created by Ashish on Dec 27.
 */
object Constants {

    const val IMDB_BASE_URL = "http://www.imdb.com/title/"
    const val OMDB_API_BASE_URL = "http://www.omdbapi.com/"

    const val DATE_PICKER_FORMAT = "%d-%d-%d"
    const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
    const val MONTH_DAY_YEAR_PATTERN = "MMM dd, yyyy"
    const val GMT_ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    const val ADAPTER_TYPE_MOVIE = 0
    const val ADAPTER_TYPE_TV_SHOW = 1
    const val ADAPTER_TYPE_PERSON = 2
    const val ADAPTER_TYPE_CREDIT = 3
    const val ADAPTER_TYPE_SEASON = 4
    const val ADAPTER_TYPE_EPISODE = 5
    const val ADAPTER_TYPE_MULTI_SEARCH = 6
    const val ADAPTER_TYPE_REVIEW = 7

    val LIST_THUMBNAIL_WIDTH = 200f.dpToPx().toInt()
    val LIST_THUMBNAIL_HEIGHT = 260f.dpToPx().toInt()
    val DETAIL_IMAGE_THUMBNAIL_SIZE = 180f.dpToPx().toInt()
}