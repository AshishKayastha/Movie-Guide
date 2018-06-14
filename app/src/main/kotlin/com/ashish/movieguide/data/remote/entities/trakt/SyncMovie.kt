package com.ashish.movieguide.data.remote.entities.trakt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncMovie(
        val rating: Int? = null,
        val ids: MovieIds? = null,
        @Json(name = "rated_at") val ratedAt: String? = null
)