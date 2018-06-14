package com.ashish.movieguide.data.remote.entities.tmdb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Images(val backdrops: List<ImageItem>? = null, val posters: List<ImageItem>? = null)