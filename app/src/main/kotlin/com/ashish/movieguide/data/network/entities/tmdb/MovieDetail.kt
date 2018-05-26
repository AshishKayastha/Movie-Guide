package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Ashish on Jan 03.
 */
@JsonClass(generateAdapter = true)
data class MovieDetail(
        val id: Long? = null,
        val title: String? = null,
        val overview: String? = null,
        val budget: Int? = null,
        val runtime: Int? = null,
        val revenue: Int? = null,
        val status: String? = null,
        val tagline: String? = null,
        val images: Images? = null,
        val videos: Videos? = null,
        val genres: List<Genre>? = null,
        val credits: CreditResults? = null,
        @Json(name = "imdb_id") val imdbId: String? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "release_date") val releaseDate: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "similar") val similarMovieResults: Results<Movie>? = null
)