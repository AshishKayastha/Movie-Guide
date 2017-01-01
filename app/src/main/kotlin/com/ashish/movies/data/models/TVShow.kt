package com.ashish.movies.data.models

import com.ashish.movies.ui.common.adapter.ViewType
import com.ashish.movies.ui.common.adapter.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json

data class TVShow(
        val id: Long? = null,
        val name: String? = null,
        val overview: String? = null,
        val popularity: Double? = null,
        @Json(name = "vote_count") val voteCount: Int? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "genre_ids") val genreIds: List<Int?>? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "original_name") val originalName: String? = null,
        @Json(name = "first_air_date") val firstAirDate: String? = null,
        @Json(name = "original_language") val originalLanguage: String? = null,
        @Json(name = "origin_country") val originCountry: List<String?>? = null
) : ViewType {

    override fun getViewType() = CONTENT_VIEW
}