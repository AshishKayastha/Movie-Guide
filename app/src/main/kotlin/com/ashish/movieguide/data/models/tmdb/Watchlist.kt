package com.ashish.movieguide.data.models.tmdb

import com.squareup.moshi.Json

data class Watchlist(
        val watchlist: Boolean? = null,
        @Json(name = "media_id") val mediaId: Long? = null,
        @Json(name = "media_type") val mediaType: String? = null
)