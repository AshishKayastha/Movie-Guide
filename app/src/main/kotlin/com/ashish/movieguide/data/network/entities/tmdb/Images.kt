package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Images(val backdrops: List<ImageItem>? = null, val posters: List<ImageItem>? = null)