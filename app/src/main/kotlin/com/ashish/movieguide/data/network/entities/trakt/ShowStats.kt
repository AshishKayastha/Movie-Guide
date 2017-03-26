package com.ashish.movieguide.data.network.entities.trakt

data class ShowStats(
        val watched: Int? = null,
        val comments: Int? = null,
        val ratings: Int? = null,
        val collected: Int? = null
)