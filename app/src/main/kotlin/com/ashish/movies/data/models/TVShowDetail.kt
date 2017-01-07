package com.ashish.movies.data.models

import com.squareup.moshi.Json

/**
 * Created by Ashish on Jan 04.
 */
data class TVShowDetail(
        val id: Long? = null,
        val name: String? = null,
        val type: String? = null,
        val status: String? = null,
        val overview: String? = null,
        val homepage: String? = null,
        val popularity: Double? = null,
        val genres: List<Genre>? = null,
        val networks: List<Network>? = null,
        val seasons: List<TVShowSeason>? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "last_air_date") val lastAirDate: String? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "first_air_date") val firstAirDate: String? = null,
        @Json(name = "number_of_seasons") val numberOfSeasons: Int? = null,
        @Json(name = "number_of_episodes") val numberOfEpisodes: Int? = null,
        @Json(name = "episode_run_time") val episodeRunTime: List<Int>? = null,
        @Json(name = "external_ids") val externalIds: ExternalIds? = null,
        @Json(name = "credits") val creditsResults: CreditResults? = null,
        @Json(name = "similar") val similarTVShowResults: Results<TVShow>? = null
)