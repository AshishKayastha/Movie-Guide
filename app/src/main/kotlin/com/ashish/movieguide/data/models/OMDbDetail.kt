package com.ashish.movieguide.data.models

data class OMDbDetail(
        val imdbId: String,
        val Rated: String? = null,
        val Country: String? = null,
        val Awards: String? = null,
        val Language: String? = null,
        val Metascore: String? = null,
        val imdbRating: String? = null,
        val imdbVotes: String? = null,
        val tomatoMeter: String? = null,
        val tomatoImage: String? = null,
        val tomatoRating: String? = null,
        val tomatoURL: String? = null,
        val tomatoUserMeter: String? = null,
        val tomatoUserRating: String? = null,
        val Production: String? = null
)