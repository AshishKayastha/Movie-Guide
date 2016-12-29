package com.ashish.movies.data.models

import com.squareup.moshi.Json

/**
 * Created by Ashish on Dec 27.
 */
data class MovieResults(
        val page: Int = 1,
        val results: List<Movie>? = null,
        @Json(name = "total_pages") val totalPages: Int = 0,
        @Json(name = "total_results") val totalResults: Int = 0
)