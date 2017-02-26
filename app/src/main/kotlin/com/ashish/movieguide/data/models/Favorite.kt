package com.ashish.movieguide.data.models

import com.squareup.moshi.Json

data class Favorite(
        val favorite: Boolean? = null,
        @Json(name = "media_id") val mediaId: Long? = null,
        @Json(name = "media_type") val mediaType: String? = null
)