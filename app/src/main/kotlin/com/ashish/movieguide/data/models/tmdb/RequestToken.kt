package com.ashish.movieguide.data.models.tmdb

import com.squareup.moshi.Json

data class RequestToken(
        val success: Boolean = false,
        @Json(name = "expires_at") val expiresAt: String? = null,
        @Json(name = "request_token") val requestToken: String? = null
)