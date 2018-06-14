package com.ashish.movieguide.data.remote.entities.trakt

data class Stats(
        val movies: MovieStats? = null,
        val shows: ShowStats? = null,
        val episodes: EpisodeStats? = null,
        val network: NetworkStats? = null,
        val ratings: RatingDistribution? = null
)