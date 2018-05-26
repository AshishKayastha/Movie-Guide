package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncEpisode(
        val rating: Int? = null,
        val ids: EpisodeIds? = null,
        @Json(name = "rated_at") val ratedAt: String? = null
)