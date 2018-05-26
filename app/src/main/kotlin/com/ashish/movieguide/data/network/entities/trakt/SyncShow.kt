package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncShow(
        val rating: Int? = null,
        val ids: ShowIds? = null,
        @Json(name = "rated_at") val ratedAt: String? = null
)