package com.ashish.movies.data.models

/**
 * Created by Ashish on Jan 09.
 */
data class FullDetailContent<out I>(
        val detailContent: I? = null,
        val omdbDetail: OMDbDetail? = null
)