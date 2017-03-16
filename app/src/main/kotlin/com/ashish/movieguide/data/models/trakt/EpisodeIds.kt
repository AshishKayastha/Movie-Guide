package com.ashish.movieguide.data.models.trakt

data class EpisodeIds(
        val tmdb: Int? = null,
        val imdb: String? = null,
        val tvdb: Int? = null,
        val tvrage: Int? = null,
        val trakt: Int? = null
)