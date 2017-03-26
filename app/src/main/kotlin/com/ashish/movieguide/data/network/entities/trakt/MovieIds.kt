package com.ashish.movieguide.data.network.entities.trakt

data class MovieIds(
        val tmdb: Long? = null,
        val imdb: String? = null,
        val trakt: Long? = null,
        val slug: String? = null
)