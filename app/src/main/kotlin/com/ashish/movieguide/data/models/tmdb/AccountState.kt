package com.ashish.movieguide.data.models.tmdb

data class AccountState(
        val favorite: Boolean = false,
        val watchlist: Boolean = false,
        val rated: Any? = null // This can be either Boolean or Rating object
)