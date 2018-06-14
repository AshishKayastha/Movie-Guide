package com.ashish.movieguide.data.remote.entities.trakt

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkStats(
        val friends: Int? = null,
        val followers: Int? = null,
        val following: Int? = null
)