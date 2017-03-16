package com.ashish.movieguide.data.models.trakt

import com.squareup.moshi.Json

data class SyncEpisode(
        val rating: Int? = null,
        val ids: EpisodeIds? = null,
        @Json(name = "rated_at") val ratedAt: String? = null
)