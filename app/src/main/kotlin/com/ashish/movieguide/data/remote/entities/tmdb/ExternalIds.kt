package com.ashish.movieguide.data.remote.entities.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by Ashish on Jan 07.
 */
@JsonClass(generateAdapter = true)
data class ExternalIds(@Json(name = "imdb_id") val imdbId: String? = null)