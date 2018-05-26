package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Favorite(
        val favorite: Boolean? = null,
        @Json(name = "media_id") val mediaId: Long? = null,
        @Json(name = "media_type") val mediaType: String? = null
)
