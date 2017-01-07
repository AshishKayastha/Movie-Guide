package com.ashish.movies.data.models

import com.squareup.moshi.Json

/**
 * Created by Ashish on Jan 07.
 */
data class ExternalIds(@Json(name = "imdb_id") val imdbId: String? = null)