package com.ashish.movieguide.data.models

import com.squareup.moshi.Json

data class SeasonDetail(
        val id: Long? = null,
        val name: String? = null,
        val images: Images? = null,
        val overview: String? = null,
        val videos: Videos? = null,
        val credits: CreditResults? = null,
        val episodes: List<Episode>? = null,
        @Json(name = "air_date") val airDate: String? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "season_number") val seasonNumber: Int? = null,
        @Json(name = "external_ids") val externalIds: ExternalIds? = null
)