package com.ashish.movieguide.data.models

data class AccountState(
        val id: Long? = null,
        val rated: Rating? = null,
        val favorite: Boolean = false,
        val watchlist: Boolean = false
)