package com.ashish.movieguide.data.network.entities.trakt

data class TraktMovieRating(
        val rating: Int? = null,
        val type: String? = null,
        val movie: TraktMovie? = null
)