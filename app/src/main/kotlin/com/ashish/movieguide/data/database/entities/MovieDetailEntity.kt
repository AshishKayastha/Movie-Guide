package com.ashish.movieguide.data.database.entities

/**
 * Created by Ashish on Feb 05.
 */
data class MovieDetailEntity(
        val movieEntity: MovieEntity,
        val omdbEntity: OMDbEntity?,
        val genres: List<GenreEntity>? = null,
        val cast: List<CreditEntity>? = null,
        val crew: List<CreditEntity>? = null,
        val images: List<ImageEntity>? = null,
        val videos: List<VideoEntity>? = null,
        val similarMovies: List<SimilarMovieEntity>? = null
)