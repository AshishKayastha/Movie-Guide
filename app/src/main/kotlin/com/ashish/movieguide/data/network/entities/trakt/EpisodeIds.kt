package com.ashish.movieguide.data.network.entities.trakt

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeIds(
        val tmdb: Long? = null,
        val imdb: String? = null,
        val tvdb: Long? = null,
        val tvrage: Long? = null,
        val trakt: Long? = null
)