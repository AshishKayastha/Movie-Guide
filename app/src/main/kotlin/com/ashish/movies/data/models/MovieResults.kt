package com.ashish.movies.data.models

import com.squareup.moshi.Json

/**
 * Created by Ashish on Dec 27.
 */
data class MovieResults(
        val page: Int? = null,
        val results: List<Movie>? = null,
        @Json(name = "total_pages") val totalPages: Int? = null,
        @Json(name = "total_results") val totalResults: Int? = null
)