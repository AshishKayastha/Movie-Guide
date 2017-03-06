package com.ashish.movieguide.data.models

data class AccountState(
        val id: Long? = null,
        val rated: Any? = null, // This can be either Boolean or Rating object
        val favorite: Boolean = false,
        val watchlist: Boolean = false
)