package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.Json

data class TraktToken(
        @Json(name = "access_token") val accessToken: String,
        @Json(name = "refresh_token") val refreshToken: String
)