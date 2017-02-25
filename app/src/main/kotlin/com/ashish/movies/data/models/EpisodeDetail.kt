package com.ashish.movies.data.models

import com.squareup.moshi.Json

data class EpisodeDetail(
        val id: Long? = null,
        val name: String? = null,
        val images: Images? = null,
        val overview: String? = null,
        val videos: Videos? = null,
        val credits: CreditResults? = null,
        @Json(name = "air_date") val airDate: String? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "still_path") val stillPath: String? = null,
        @Json(name = "season_number") val seasonNumber: Int? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "episode_number") val episodeNumber: Int? = null,
        @Json(name = "external_ids") val externalIds: ExternalIds? = null
)