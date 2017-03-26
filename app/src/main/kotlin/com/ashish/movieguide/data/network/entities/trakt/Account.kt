package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.Json

data class Account(
        val timezone: String? = null,
        @Json(name = "cover_image") val coverImage: String? = null,
        @Json(name = "time_24hr") val time24hr: Boolean? = null
)