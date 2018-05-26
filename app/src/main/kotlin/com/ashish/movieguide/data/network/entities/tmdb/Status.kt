package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Status(
        val success: Boolean? = null,
        @Json(name = "status_code") val statusCode: Int? = null,
        @Json(name = "status_message") val statusMessage: String? = null
)