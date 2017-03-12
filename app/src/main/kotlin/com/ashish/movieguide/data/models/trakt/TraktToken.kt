package com.ashish.movieguide.data.models.trakt

import com.squareup.moshi.Json

data class TraktToken(
        @Json(name = "access_token") val accessToken: String,
        @Json(name = "refresh_token") val refreshToken: String
)