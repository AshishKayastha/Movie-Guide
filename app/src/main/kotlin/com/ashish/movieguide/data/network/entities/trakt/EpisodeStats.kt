package com.ashish.movieguide.data.network.entities.trakt

data class EpisodeStats(
        val watched: Int? = null,
        val plays: Int? = null,
        val comments: Int? = null,
        val minutes: Int? = null,
        val ratings: Int? = null,
        val collected: Int? = null
)