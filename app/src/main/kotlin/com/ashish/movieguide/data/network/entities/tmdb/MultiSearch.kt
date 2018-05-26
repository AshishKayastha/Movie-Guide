package com.ashish.movieguide.data.network.entities.tmdb

import com.ashish.movieguide.ui.common.adapter.ViewType
import com.ashish.movieguide.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MultiSearch(
        val id: Long? = null,
        val name: String? = null,
        val title: String? = null,
        @Json(name = "media_type") val mediaType: String? = null,
        @Json(name = "release_date") val releaseDate: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "profile_path") val profilePath: String? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "first_air_date") val firstAirDate: String? = null
) : ViewType {

    override fun getViewType() = CONTENT_VIEW
}