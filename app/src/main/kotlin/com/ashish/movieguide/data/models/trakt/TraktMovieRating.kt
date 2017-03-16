package com.ashish.movieguide.data.models.trakt

data class TraktMovieRating(
        val rating: Int? = null,
        val type: String? = null,
        val movie: TraktItem? = null
)