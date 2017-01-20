package com.ashish.movies.data.models

import com.squareup.moshi.Json

/**
 * Created by Ashish on Dec 31.
 */
data class Results<out I>(
        val page: Int = 1,
        val results: List<I>? = null,
        @Json(name = "total_pages") val totalPages: Int = 0
)