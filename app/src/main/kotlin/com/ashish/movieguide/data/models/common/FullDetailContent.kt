package com.ashish.movieguide.data.models.common

/**
 * A wrapper class to wrap contents provided by TMDb, OMDb and Trakt
 *
 * @param I TMDb Item
 * @param T Trakt Item
 */
data class FullDetailContent<out I, out T>(
        val detailContent: I? = null,
        val omdbDetail: OMDbDetail? = null,
        val traktItem: T? = null
)