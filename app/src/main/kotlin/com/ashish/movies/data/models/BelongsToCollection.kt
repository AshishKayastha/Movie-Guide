package com.ashish.movies.data.models

import com.squareup.moshi.Json

data class BelongsToCollection(
        val id: Long? = null,
        val name: String? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null
)