package com.ashish.movieguide.data.remote.entities.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OMDbDetail(
        val imdbID: String? = null,
        val Rated: String? = null,
        val Country: String? = null,
        val Awards: String? = null,
        val Language: String? = null,
        val Ratings: List<Ratings>? = null,
        val Metascore: String? = null,
        val imdbRating: String? = null,
        val imdbVotes: String? = null,
        val tomatoURL: String? = null,
        val Production: String? = null
)