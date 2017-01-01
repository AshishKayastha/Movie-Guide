package com.ashish.movies.data.models

import com.squareup.moshi.Json

data class MovieDetail(
        val id: Long? = null,
        val title: String? = null,
        val overview: String? = null,
        val runtime: Int? = null,
        val budget: Int? = null,
        val revenue: Int? = null,
        val status: String? = null,
        val tagline: String? = null,
        val popularity: Double? = null,
        val genres: List<GenresItem>? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "imdb_id") val imdbId: String? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "release_date") val releaseDate: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "belongs_to_collection") val belongsToCollection: BelongsToCollection? = null
)