package com.ashish.movieguide.data.network.entities.tmdb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Videos(val results: List<VideoItem>? = null)