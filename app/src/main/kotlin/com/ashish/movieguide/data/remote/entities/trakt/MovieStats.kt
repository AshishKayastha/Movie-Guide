package com.ashish.movieguide.data.remote.entities.trakt

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieStats(
        val watched: Int? = null,
        val plays: Int? = null,
        val comments: Int? = null,
        val minutes: Int? = null,
        val ratings: Int? = null,
        val collected: Int? = null
)