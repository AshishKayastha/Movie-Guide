package com.ashish.movies.data.models

import com.ashish.movies.ui.common.adapter.ViewType
import com.squareup.moshi.Json

data class Episode(
        val id: Long? = null,
        val name: String? = null,
        val overview: String? = null,
        val crew: List<Credit>? = null,
        @Json(name = "air_date") val airDate: String? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "still_path") val stillPath: String? = null,
        @Json(name = "season_number") val seasonNumber: Int? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "episode_number") val episodeNumber: Int? = null,
        @Json(name = "guest_stars") val guestStars: List<Credit>? = null
) : ViewType {

    override fun getViewType() = ViewType.CONTENT_VIEW
}