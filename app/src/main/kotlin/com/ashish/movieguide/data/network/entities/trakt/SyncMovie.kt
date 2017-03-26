package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.Json

data class SyncMovie(
        val rating: Int? = null,
        val ids: MovieIds? = null,
        @Json(name = "rated_at") val ratedAt: String? = null
)