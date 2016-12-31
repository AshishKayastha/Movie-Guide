package com.ashish.movies.data.models

import com.squareup.moshi.Json

data class TVShowResults(
        val page: Int = 1,
        val results: List<TVShow>? = null,
        @Json(name = "total_pages") val totalPages: Int = 0,
        @Json(name = "total_results") val totalResults: Int = 0
)