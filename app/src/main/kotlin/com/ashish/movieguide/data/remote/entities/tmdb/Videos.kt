package com.ashish.movieguide.data.remote.entities.tmdb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Videos(val results: List<VideoItem>? = null)