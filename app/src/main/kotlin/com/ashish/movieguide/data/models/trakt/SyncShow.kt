package com.ashish.movieguide.data.models.trakt

import com.squareup.moshi.Json

data class SyncShow(
        val rating: Int? = null,
        val ids: ShowIds? = null,
        @Json(name = "rated_at") val ratedAt: String? = null
)