package com.ashish.movieguide.data.remote.entities.trakt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Account(
        val timezone: String? = null,
        @Json(name = "cover_image") val coverImage: String? = null,
        @Json(name = "time_24hr") val time24hr: Boolean? = null
)