package com.ashish.movies.data.database.entities

/**
 * Created by Ashish on Feb 05.
 */
data class MovieEntity(
        val id: Long,
        val title: String? = null,
        val overview: String? = null,
        val voteCount: Int? = null,
        val posterPath: String? = null,
        val releaseDate: String? = null,
        val voteAverage: Double? = null,
        val backdropPath: String? = null,
        val runtime: Int? = null,
        val budget: Int? = null,
        val revenue: Int? = null,
        val status: String? = null,
        val tagline: String? = null,
        val imdbId: String? = null
)