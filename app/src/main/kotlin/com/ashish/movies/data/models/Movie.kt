package com.ashish.movies.data.models

import com.ashish.movies.ui.common.ViewType
import com.ashish.movies.ui.common.ViewType.Companion.CONTENT_VIEW
import com.squareup.moshi.Json

/**
 * Created by Ashish on Dec 27.
 */
data class Movie(
        val id: Int? = null,
        val title: String? = null,
        val overview: String? = null,
        val video: Boolean? = null,
        val adult: Boolean? = null,
        val popularity: Double? = null,
        @Json(name = "original_title") val originalTitle: String? = null,
        @Json(name = "original_language") val originalLanguage: String? = null,
        @Json(name = "genre_ids") val genreIds: List<Int?>? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "release_date") val releaseDate: String? = null,
        @Json(name = "vote_average") val voteAverage: Double? = null,
        @Json(name = "vote_count") val voteCount: Int? = null
) : ViewType {

    override fun getViewType() = CONTENT_VIEW
}