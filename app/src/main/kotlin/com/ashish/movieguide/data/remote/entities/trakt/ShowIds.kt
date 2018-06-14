package com.ashish.movieguide.data.remote.entities.trakt

data class ShowIds(
        val tmdb: Long? = null,
        val imdb: String? = null,
        val tvdb: Long? = null,
        val tvrage: Long? = null,
        val trakt: Long? = null,
        val slug: String? = null
)