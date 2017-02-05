package com.ashish.movies.data.database.entities

import com.ashish.movies.data.models.Credit

/**
 * Created by Ashish on Feb 05.
 */
data class MovieDetailEntity(
        val movieEntity: MovieEntity,
        val genres: List<GenreEntity>? = null,
        val cast: List<Credit>? = null,
        val crew: List<Credit>? = null,
        val images: List<ImageEntity>? = null,
        val videos: List<VideoEntity>? = null,
        val similarMovies: List<MovieEntity>? = null
)