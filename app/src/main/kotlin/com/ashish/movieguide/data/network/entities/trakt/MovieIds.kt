package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieIds(
        val tmdb: Long? = null,
        val imdb: String? = null,
        val trakt: Long? = null,
        val slug: String? = null
)