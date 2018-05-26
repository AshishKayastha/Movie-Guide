package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Ashish on Dec 31.
 */
@JsonClass(generateAdapter = true)
data class Results<out I>(
        val page: Int = 1,
        val results: List<I>? = null,
        @Json(name = "total_pages") val totalPages: Int = 0
)