package com.ashish.movieguide.data.models.trakt

data class Stats(
        val movies: MovieStats? = null,
        val shows: ShowStats? = null,
        val episodes: EpisodeStats? = null,
        val network: NetworkStats? = null,
        val ratings: Ratings? = null
)