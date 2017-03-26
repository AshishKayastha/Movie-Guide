package com.ashish.movieguide.data.network.entities.trakt

data class PersonIds(
        val tmdb: Long? = null,
        val imdb: String? = null,
        val tvrage: Long? = null,
        val trakt: Long? = null,
        val slug: String? = null
)