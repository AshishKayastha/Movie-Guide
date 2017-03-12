package com.ashish.movieguide.data.models.trakt

import com.squareup.moshi.Json

data class TokenRequest(
        val code: String,
        @Json(name = "client_id") val clientId: String,
        @Json(name = "client_secret") val clientSecret: String,
        @Json(name = "redirect_uri") val redirectUri: String,
        @Json(name = "grant_type") val grantType: String
)