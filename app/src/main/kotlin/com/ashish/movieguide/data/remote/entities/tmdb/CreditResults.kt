package com.ashish.movieguide.data.remote.entities.tmdb

import com.squareup.moshi.JsonClass

/**
 * Created by Ashish on Jan 03.
 */
@JsonClass(generateAdapter = true)
data class CreditResults(val cast: List<Credit>? = null, val crew: List<Credit>? = null)